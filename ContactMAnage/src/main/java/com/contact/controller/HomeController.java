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
	
	@Autowired(required=true)
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model) {	
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {	
		return "about";
	}
	
	@RequestMapping("/signup")
	public String service(Model model) {	
		model.addAttribute("user",new User());
		return "signup";	
	}
	
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") User user, @RequestParam(value="agreement",defaultValue="false") boolean agreement,
			Model model, HttpSession session ) {
		
		
		try {
			
			if(!agreement) {
				
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception("You have not agreed terms and conditions.");
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("imageurl");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
		
			
			this.userRepository.save(user);
			
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

	@RequestMapping("/signin")
	public String customLogin(Model model) {	
		
		return "login";
	}
}
