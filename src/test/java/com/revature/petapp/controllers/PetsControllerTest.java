package com.revature.petapp.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.petapp.PetApp2Application;
import com.revature.petapp.beans.Pet;
import com.revature.petapp.services.EmployeeService;
import com.revature.petapp.services.UserService;

@SpringBootTest(classes=PetApp2Application.class)
public class PetsControllerTest {
	@MockBean
	private UserService userServ;
	@MockBean
	private EmployeeService empServ;
	@Autowired
	private PetsController petsCtrl;
	
	// this object basically represents a mock of the Spring Web architecture
	private static MockMvc mockMvc;
	
	// this is a Jackson object mapper for JSON marshalling
	// (turning objects to JSON strings and vice versa
	private ObjectMapper objMapper = new ObjectMapper();
	
	@BeforeAll
	public static void setUp() {
		// sets up the minimum architecture to test our controller
		mockMvc = MockMvcBuilders.standaloneSetup(PetsController.class).build();
	}
	
	@Test
	public void getAvailablePets() throws Exception {
		when(userServ.viewAvailablePets()).thenReturn(Collections.emptySet());
		
		String jsonSet = objMapper.writeValueAsString(Collections.emptySet());
		
		mockMvc.perform(get("/pets"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(jsonSet))
			.andReturn();
	}
	
	@Test
	public void addPetSuccessfully() throws Exception {
		Pet newPet = new Pet();
		when(empServ.addNewPet(newPet)).thenReturn(1);
		
		String jsonPet = objMapper.writeValueAsString(newPet);
		
		mockMvc.perform(post("/pets").content(jsonPet).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andReturn();
	}
}
