package com.revature.petapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.petapp.beans.Person;

// to have Spring Data write the DAO methods, you have your interface
// extend JpaRepository and set the object type and the primary key type
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
	// when you extend the JpaRepository, Spring will write the basic CRUD methods:
	// findAll, findById, save (this does create and update), delete
	
	// we can also have custom methods that Spring will also write
	// we just have to write the method signature following a specific pattern
	// findByField(FieldType field)
	public Person findByUsername(String username);
	
	// findByUsernameAndPassword(String username, String password);
	
	// findByAgeGreaterThan(int greaterThan);
	// findBySupervisorNotNull(Employee supervisor);
	// findBySpeciesContainingIgnoreCase(String speciesSearch);
}
