package com.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.contact.dao.ContactRepository;
import com.contact.dao.UserRepository;
import com.contact.entites.Contact;
import com.contact.entites.User;

@SpringBootTest
class ContactMAnageApplicationTests {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ContactRepository contactRepository;

	@Test
	void contextLoads() {
		
	}	
	
	/*
	 * Create user record in User Table database case.
	 * */
	@Test
	public void userTest() {
		User user = new User();
		user.setName("testing1");
		user.setEmail("testing1@gmail.com");
		user.setAbout("testing1 About");
		user.setContact(null);
		user.setEnabled(true);
		user.setImageUrl("imageurl");
		user.setPassword("1234");
		user.setRole("ROLE_USER");

		userRepository.save(user);
		assertNotNull(userRepository);
	}
	
	/*
	 * Create contact record in Contact Table database case.
	 * */
	@Test
	public void contactTest() {
		Contact contact = new Contact();
		contact.setName("contact testing1");
		contact.setNickName("contact testing nickname");
		contact.setEmail("contact testing1@gmail.com");
		contact.setImageurl("contact imageurl");
		contact.setPhone("9885676542");
		contact.setDescription("contact description");
		contact.setWork("contact testing work");
		contact.setUser(null);
		
		contactRepository.save(contact);
		assertNotNull(contactRepository);
	}
	
	/*
	 * Delete User Record from User Database  Test case.
	 * */
	@Test
	public void testDeleteUser() {
		userRepository.deleteById(75);
		assertThat(userRepository.existsById(75)).isFalse();
		
	}
	
	/*
	 * Delete Contact record from Contact Database Test case.
	 * */
	@Test
	public void testDeleteContact() {
		contactRepository.deleteById(53);
		assertThat(contactRepository.existsById(53)).isFalse();
		
	}
	
}
