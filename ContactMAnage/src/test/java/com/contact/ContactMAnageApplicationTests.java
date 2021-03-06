package com.contact;

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
	
}
