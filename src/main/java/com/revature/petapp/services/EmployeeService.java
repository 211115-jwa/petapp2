package com.revature.petapp.services;

import com.revature.petapp.beans.Pet;

public interface EmployeeService {
	// services represent business logic - actual user activities.
	// what can an employee do?
	public int addNewPet(Pet newPet);
	public Pet editPet(Pet petToEdit);
	public Pet getPetById(int id);
}
