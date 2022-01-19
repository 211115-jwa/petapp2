package com.revature.petapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.revature.petapp.PetApp2Application;
import com.revature.petapp.beans.Pet;
import com.revature.petapp.data.PetRepository;

// brings in mockito, etc. and allows you to use the ioc container
@SpringBootTest(classes=PetApp2Application.class)
public class EmployeeServiceTest {
	// this allows Spring to make a mock of a Spring bean
	@MockBean
	private PetRepository petRepo;
	@Autowired
	private EmployeeService empServ;
	
	@Test
	public void addNewPetSuccessfully() {
		Pet newPet = new Pet();
		Pet mockPet = new Pet();
		mockPet.setId(10);
				
		when(petRepo.save(newPet)).thenReturn(mockPet);
		
		int newId = empServ.addNewPet(newPet);
		
		assertNotEquals(0, newId);
	}
	
	@Test
	public void addNewPetSomethingWrong() {
		Pet pet = new Pet();
		
		when(petRepo.save(pet)).thenReturn(pet);
		
		int newId = empServ.addNewPet(pet);
		
		assertEquals(0,newId);
	}
	
	@Test
	public void editPetSuccessfully() {
		Pet editedPet = new Pet();
		editedPet.setId(2);
		editedPet.setAge(10);
		
		when(petRepo.findById(2)).thenReturn(Optional.of(editedPet));
		doNothing().when(petRepo).save(Mockito.any(Pet.class));
		
		Pet actualPet = empServ.editPet(editedPet);
		
		assertEquals(editedPet, actualPet);
	}
	
	@Test
	public void editPetSomethingWrong() {
		Pet mockPet = new Pet();
		mockPet.setId(2);
		
		when(petRepo.findById(2)).thenReturn(Optional.of(mockPet));
		doNothing().when(petRepo).save(Mockito.any(Pet.class));
		
		Pet editedPet = new Pet();
		editedPet.setId(2);
		editedPet.setAge(10);
		
		Pet actualPet = empServ.editPet(editedPet);
		
		assertNotEquals(editedPet, actualPet);
	}
	
	@Test
	public void editPetDoesNotExist() {
		when(petRepo.findById(2)).thenReturn(Optional.ofNullable(null));
		
		Pet editedPet = new Pet();
		editedPet.setId(2);
		editedPet.setAge(10);
		
		Pet actualPet = empServ.editPet(editedPet);
		
		assertNull(actualPet);
		verify(petRepo, times(0)).save(Mockito.any(Pet.class));
	}
	
	@Test
	public void getByIdPetExists() {
		Pet pet = new Pet();
		pet.setId(2);
		
		when(petRepo.findById(2)).thenReturn(Optional.of(pet));
		
		Pet actualPet = empServ.getPetById(2);
		assertEquals(pet, actualPet);
	}
	
	@Test
	public void getByIdPetDoesNotExist() {
		when(petRepo.findById(2)).thenReturn(Optional.ofNullable(null));
		
		Pet actualPet = empServ.getPetById(2);
		assertNull(actualPet);
	}
}
