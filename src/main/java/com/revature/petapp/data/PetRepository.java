package com.revature.petapp.data;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.petapp.beans.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
	public Set<Pet> findByStatusName(String statusName);
}
