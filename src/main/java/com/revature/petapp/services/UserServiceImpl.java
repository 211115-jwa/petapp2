package com.revature.petapp.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.petapp.beans.Person;
import com.revature.petapp.beans.Pet;
import com.revature.petapp.beans.Status;
import com.revature.petapp.data.PersonRepository;
import com.revature.petapp.data.PetRepository;
import com.revature.petapp.data.StatusRepository;
import com.revature.petapp.exceptions.AlreadyAdoptedException;
import com.revature.petapp.exceptions.IncorrectCredentialsException;
import com.revature.petapp.exceptions.UsernameAlreadyExistsException;

@Service
public class UserServiceImpl implements UserService {
	private PersonRepository personRepo;
	private PetRepository petRepo;
	private StatusRepository statusRepo;
	
	// constructor injection
	@Autowired
	public UserServiceImpl(PersonRepository personRepo,
			PetRepository petRepo,
			StatusRepository statusRepo) {
		this.personRepo = personRepo;
		this.petRepo = petRepo;
		this.statusRepo = statusRepo;
	}

	@Override
	@Transactional
	public Person register(Person newUser) throws UsernameAlreadyExistsException {
		int newId = personRepo.save(newUser).getId();
		if (newId > 0) {
			newUser.setId(newId);
			return newUser;
		} else if (newId == -1) {
			throw new UsernameAlreadyExistsException();
		}
		return null;
	}

	@Override
	public Person logIn(String username, String password) throws IncorrectCredentialsException {
		Person personFromDatabase = personRepo.findByUsername(username);
		if (personFromDatabase != null && personFromDatabase.getPassword().equals(password)) {
			return personFromDatabase;
		} else {
			throw new IncorrectCredentialsException();
		}
	}
	
	@Override
	public Person getUserById(int id) {
		return personRepo.findById(id).get();
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Person updateUser(Person userToUpdate) {
		if (personRepo.existsById(userToUpdate.getId())) {
			personRepo.save(userToUpdate);
			userToUpdate = personRepo.findById(userToUpdate.getId()).get();
			return userToUpdate;
		}
		return null;
	}

	@Override
	@Transactional
	public Person adoptPet(int petId, Person newOwner) throws AlreadyAdoptedException {
		Pet petToAdopt = petRepo.findById(petId).get();
		if (petToAdopt!=null && petToAdopt.getStatus().getName().equals("Available")) {
			Status adoptedStatus = statusRepo.findByName("Adopted");
			petToAdopt.setStatus(adoptedStatus);
			newOwner.getPets().add(petToAdopt);
			
			petRepo.save(petToAdopt);
			this.updateUser(newOwner);
			//personRepo.save(newOwner);
			return newOwner;
		} else {
			throw new AlreadyAdoptedException();
		}
	}

	@Override
	public Set<Pet> viewAvailablePets() {
		return petRepo.findByStatusName("Available");
	}

	@Override
	public Set<Pet> searchAvailablePetsBySpecies(String species) {
		Set<Pet> availablePets = petRepo.findByStatusName("Available");

		availablePets = availablePets.stream()
				.filter(pet -> pet.getSpecies().toLowerCase().contains(species.toLowerCase()))
				.collect(Collectors.toSet());
		
		return availablePets;
	}
}
