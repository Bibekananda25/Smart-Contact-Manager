/*
 * @:Bibekananda
 * 
 * This is a controller class it contain some methods.
 * 
 * */


package com.contact.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.dao.UserRepository;
import com.contact.entites.User;
import com.contact.helper.Message;

@Controller
public class HomeController {
	
	/*
	 * This variable is used to encrypt the password of user.
	 * */
	@Autowired(required=true)
	private BCryptPasswordEncoder passwordEncoder;
	
	/*
	 * This variable is used for User Entity class for creating repository object.  
	 * */
	@Autowired
	private UserRepository userRepository;
	
	/*
	 * This method returns home page.
	 * */
	@RequestMapping("/")
	public String home(Model model) {	
		return "home";
	}
	
	/*
	 * This method returns about page.
	 * */
	@RequestMapping("/about")
	public String about(Model model) {	
		return "about";
	}
	

	/*
	 * This method returns signup page .
	 * */
	@RequestMapping("/signup")
	public String service(Model model) {	
		model.addAttribute("user",new User());
		return "signup";	
	}
	

	/*
	 * This method acts as a register for of a user where user enter some necessary inputs for creating account.
	 * there is a check box for terms and conditions.
	 * */
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") User user, @RequestParam(value="agreement",defaultValue="false") boolean agreement,
			Model model, HttpSession session ) {
		
		try {
			
			if(!agreement) {	
				
				throw new Exception("You have not agreed terms and conditions.");
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("imageurl");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully Registred!!","alert-success"));
			return "signup";			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Somthing went Wrong!!"+e.getMessage(),"alert-danger"));
			return "signup";
		}		
	}
	


	/*
	 * This method returns login page for user login.
	 * */
	@RequestMapping("/signin")
	public String customLogin(Model model) {		
		return "login";
	}
}
