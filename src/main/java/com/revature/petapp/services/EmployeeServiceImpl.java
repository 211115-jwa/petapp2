package com.revature.petapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revature.petapp.beans.Pet;
import com.revature.petapp.data.PetRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	// the PetRepository (petDAO) is necessary for the EmployeeService,
	// therefore it is a dependency of the EmployeeService.
	private PetRepository petRepo;
	
	// constructor injection
	@Autowired
	public EmployeeServiceImpl(PetRepository petRepo) {
		this.petRepo=petRepo;
	}

	@Override
	@Transactional
	public int addNewPet(Pet newPet) {
		//return petDao.create(newPet);
		return petRepo.save(newPet).getId();
	}

	@Override
	@Transactional
	public Pet editPet(Pet petToEdit) {
		Optional<Pet> petFromDatabase = petRepo.findById(petToEdit.getId());
		if (petFromDatabase.isPresent()) {
			petRepo.save(petToEdit);
			return petRepo.findById(petToEdit.getId()).get();
		}
		return null;
	}

	@Override
	public Pet getPetById(int id) {
		return petRepo.findById(id).get();
	}

}
