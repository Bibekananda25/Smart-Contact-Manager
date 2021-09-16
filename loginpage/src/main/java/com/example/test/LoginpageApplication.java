package com.example.test;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.test.dao.UserRepository;
import com.example.test.entities.User;

@SpringBootApplication
public class LoginpageApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(LoginpageApplication.class, args);
		UserRepository userservice = context.getBean(UserRepository.class);
//		User user = new User();
//		user.setId(102);
//		user.setName("Ram");
//		user.setAddress("Cuttack");
//		user.setAge(25);
//		User user1 = userservice.save(user);
//		System.out.println(user1);
		List<User> users =  userservice.findByName("Bibekananda");
		users.forEach(e->System.out.println(e));
	}

}
