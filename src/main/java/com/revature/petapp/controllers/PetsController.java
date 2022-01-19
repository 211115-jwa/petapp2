package com.revature.petapp.controllers;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.petapp.annotations.Authenticate;
import com.revature.petapp.beans.Person;
import com.revature.petapp.beans.Pet;
import com.revature.petapp.exceptions.AlreadyAdoptedException;
import com.revature.petapp.services.EmployeeService;
import com.revature.petapp.services.UserService;

//@Controller // allows returning views by default
@RestController // basically puts @ResponseBody over ALL methods - no returning views
@RequestMapping(path="/pets") // all requests starting with /pets come to this controller
@CrossOrigin(origins="http://localhost:4200") // accepts requests from angular
public class PetsController {
	private static UserService userServ;
	private static EmployeeService empServ;
	
	public PetsController() { super(); }
	
	@Autowired
	public PetsController(UserService userServ, EmployeeService empServ) {
		this.userServ = userServ;
		this.empServ = empServ;
	}
	
	private static Logger log = LogManager.getLogger(PetsController.class);

	//@ResponseBody // this method does NOT return a view, it just returns data
	//@RequestMapping(method=RequestMethod.GET)
	@GetMapping
	public ResponseEntity<Set<Pet>> getPets() {
		log.info("user is getting all available pets");
		
		Set<Pet> availablePets = userServ.viewAvailablePets();
		return ResponseEntity.ok(availablePets);
		//return ResponseEntity.status(HttpStatus.OK).body(availablePets);
	}
	
	@Authenticate(requiredRoles={"Employee"})
	@PostMapping
	public ResponseEntity<Void> addPet(@RequestBody Pet newPet) {
		log.info("user is adding a pet");
		System.out.println(newPet);
		log.debug("pet object from request body: " + newPet);
		
		if (newPet !=null) {
			empServ.addNewPet(newPet);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@Authenticate(requiredRoles = {})
	@PutMapping(path="/adopt/{petId}")
	public ResponseEntity<Person> adoptPet(@RequestBody Person newOwner,
			@PathVariable int petId) {
		log.info("user is adopting a pet");
		
		try {
			log.debug("id of pet to adopt: " + petId);
			log.debug("person adopting pet: " + newOwner);
			
			// returns the person with their new pet added
			newOwner = userServ.adoptPet(petId, newOwner);
			return ResponseEntity.ok(newOwner);
		} catch (AlreadyAdoptedException e) {
			log.error("AlreadyAdoptedException");
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
	
	@GetMapping(path="/{petId}")
	public ResponseEntity<Pet> getPetById(@PathVariable int petId) {
		log.info("user is getting a pet by id");
		log.debug("pet id: " + petId);
		
		Pet pet = empServ.getPetById(petId);
		if (pet != null)
			return ResponseEntity.ok(pet);
		else
			return ResponseEntity.notFound().build();
	}
	
	@Authenticate(requiredRoles={"Employee"})
	@PutMapping(path="/{petId}")
	public ResponseEntity<Pet> updatePet(@PathVariable int petId,
			@RequestBody Pet petToEdit) {
		log.info("user is updating a pet");
		log.debug("pet id to update: " + petId);
		log.debug("pet to update from request body: " + petToEdit);
		
		if (petToEdit != null && petToEdit.getId() == petId) {
			petToEdit = empServ.editPet(petToEdit);
			if (petToEdit != null)
				return ResponseEntity.ok(petToEdit);
			else
				return ResponseEntity.notFound().build();
		} else {
			// conflict: the id doesn't match the id of the pet sent
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
}
