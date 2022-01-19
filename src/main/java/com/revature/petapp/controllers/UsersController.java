package com.revature.petapp.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import com.revature.petapp.exceptions.IncorrectCredentialsException;
import com.revature.petapp.exceptions.UsernameAlreadyExistsException;
import com.revature.petapp.services.UserService;

@RestController
@RequestMapping(path="/users")
@CrossOrigin(origins="http://localhost:4200")
public class UsersController {
	private static UserService userServ;
	
	// default constructor
	public UsersController() { super(); }
	
	@Autowired
	public UsersController(UserService userServ) {
		this.userServ=userServ;
	}
	
	// POST to /users
	public ResponseEntity<Map<String,Integer>> register(@RequestBody Person newUser) {
		try {
			newUser = userServ.register(newUser);
			Map<String, Integer> newIdMap = new HashMap<>();
			newIdMap.put("generatedId", newUser.getId());
			return ResponseEntity.status(HttpStatus.CREATED).body(newIdMap);
		} catch (UsernameAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
	
	// POST to /users/auth
	@PostMapping(path="/auth")
	public ResponseEntity<String> logIn(@RequestBody Map<String, String> credentials) {
		String username = credentials.get("username");
		String password = credentials.get("password");
		
		try {
			Person person = userServ.logIn(username, password);
			String token = Integer.toString(person.getId());
			return ResponseEntity.ok(token);
		} catch (IncorrectCredentialsException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	// GET to /users/{userId}/auth
	@GetMapping(path="/{userId}/auth")
	public ResponseEntity<Person> checkLogin(@PathVariable int userId) {
		Person loggedInPerson = userServ.getUserById(userId);
		if (loggedInPerson!=null) {
			return ResponseEntity.ok(loggedInPerson);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	// GET to /users/{userId}
	@GetMapping(path="/{userId}")
	public ResponseEntity<Person> getUserById(@PathVariable int userId) {
		Person user = userServ.getUserById(userId);
		if (user != null)
			return ResponseEntity.ok(user);
		else
			return ResponseEntity.notFound().build();
	}
	
	// PUT to /users/{userId}
	@Authenticate(requiredRoles={})
	@PutMapping(path="/{userId}")
	public ResponseEntity<Person> updateUser(@RequestBody Person userToEdit,
			@PathVariable int userId) {
		if (userToEdit != null && userToEdit.getId() == userId) {
			userToEdit = userServ.updateUser(userToEdit);
			if (userToEdit != null)
				return ResponseEntity.ok(userToEdit);
			else
				return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
}
