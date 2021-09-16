package com.example.test.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.test.entities.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	public List<User> findByName(String name);

}
