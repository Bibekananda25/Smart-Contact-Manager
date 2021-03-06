/*
 * This interface is repository for User entity class. 
 * */

package com.contact.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contact.entites.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	/*
	 * This method is used to find all the details of user by using user email.
	 * */
	@Query("select u from User u where u.email = :email")
	public User getUserByUserName(@Param("email") String email);

}
