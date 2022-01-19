package com.revature.petapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.petapp.beans.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
	public Status findByName(String name);
}
