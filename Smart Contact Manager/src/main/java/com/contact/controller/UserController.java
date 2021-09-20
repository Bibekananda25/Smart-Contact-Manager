/*
 * @Author:BIbekananda
 * 
 * Thus is user controller containing all URL of user without login this controller can not run.
 * */


package com.contact.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contact.dao.ContactRepository;
import com.contact.dao.UserRepository;
import com.contact.entites.Contact;
import com.contact.entites.User;
import com.contact.helper.Message;


/*
 * /user is URL for user to login the form. 
 * */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;
	
	/*
	 * The model represents a Java object carrying data. The view visualizes the data that the model contains.
	 * 
	 * The principal is the currently logged in user.
	 * 
	 * The method addCommonData is used to find out the currently user login details and send it for further use.
	 * */

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);		
		model.addAttribute("user", user);
		
	}
	
	
	/*
	 * After hitting /index URL the method dashboard will execute and return "userHomepage.html" that contain user's home page with user details.
	 * */

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {

		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);
		model.addAttribute("user", user);
		return "userHomepage";
	}

	
	/*
	 * This method is called when user click the button that link with URL /add_contact.
	 * This method returns "contactPage.html" which contain all the details of a contact to be added under that user.
	 * */

	@RequestMapping("/add_contact")
	public String contactDashboard(Model model) {
		model.addAttribute("contact", new Contact());
		return "contactPage";
	}

	
	/*
	 * The method processContact is used to take of contact details if the image file is blank then it will take a default profile image.
	 * Otherwise it will take user input image.
	 * If the form submit successfully then it will show successful message otherwise error message will be displayed on the screen.  
	 * */

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("image") MultipartFile file,
			Principal principal, HttpSession session) {

		try {
			
			String name = principal.getName();
			User user = userRepository.getUserByUserName(name);
			if (file.isEmpty()) {
				contact.setImageurl("contact.png");

			} else {
				contact.setImageurl(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			contact.setUser(user);
			user.getContact().add(contact);
			this.userRepository.save(user);
			session.setAttribute("message", new Message("Your contact is added!!", "success"));

		} catch (Exception e) {			
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong!!", "danger"));
		}
		return "contactPage";
	}

	
	/*
	 * The method showContact is used to show all the contact in pagination format.
	 * By default there are four contact will be displayed on a single page. According to requirement we can change the number of 
	 * contact to be displayed in one page.
	 * */

	@RequestMapping("/show_contact/{page}")
	public String showContact(@PathVariable("page") Integer page,
			Model model, Principal principal) {

		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);
		Pageable pageable = PageRequest.of(page, 4);
		Page<Contact> findContactsByUser = contactRepository.findContactsByUser(user.getUserId(), pageable);
		model.addAttribute("contacts", findContactsByUser);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPage", findContactsByUser.getTotalPages());
		return "show_Contact";
	}


	
	/*
	 * The method showContactDetails is used to display all details of a single contact and we can update and delete the contact as well.  
	 * */

	@RequestMapping("/{cId}/contact")
	public String showContactDetails(@PathVariable("cId") Integer cId,
			Model model, Principal principal) {

		Optional<Contact> contactOptional = contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);
		if (user.getUserId() == contact.getUser().getUserId())
			model.addAttribute(contact);
		return "contact_details";
	}
	
	
	
	
	
	
	
	/*
	 * The method deleteContact is used to delete a particular contact.The contact details also delete from database. 
	 * */

	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId,
			Model model, 
			Principal principal,
			HttpSession session) {
		
		Optional<Contact> contactOptional = contactRepository.findById(cId);
		Contact contact = contactOptional.get();		
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);
		if (user.getUserId() == contact.getUser().getUserId()) {
			User user1 = userRepository.getUserByUserName(principal.getName());
			user1.getContact().remove(contact);
			userRepository.save(user);
			System.out.println("DELETED");
		}
		session.setAttribute("message", new Message("Contact Deleted Successfully..", "success"));
		return "redirect:/user/show_contact/0";

	}

	
	/*
	 * The method updateForm is used to display all the details of the contact to be updated.
	 * */

	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model model) {

		Contact contact = contactRepository.findById(cid).get();
		model.addAttribute("contact", contact);
		return "update-form";
	}

	
	/*
	 * The method updateHandler is used to update all the details of contact according to user requirement.
	 * If the image is no updated by user then it will show existing image otherwise it will use the image updated by user.
	 * */
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact, 
			@RequestParam("image") MultipartFile file, Model model,
			HttpSession session, Principal principal) {
		
		try {

			Contact oldContactDetails = contactRepository.findById(contact.getcId()).get();
			if (!file.isEmpty()) {
				
				// Delete old profile Image
				File deleteFile = new ClassPathResource("static/image").getFile();
				File file2 = new File(deleteFile, oldContactDetails.getImageurl());
				file2.delete();
				
				// update new profile Image
				File saveFile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImageurl(file.getOriginalFilename());
			} else {
				contact.setImageurl(oldContactDetails.getImageurl());
			}
			session.setAttribute("message", new Message("YOUR CONTACT UPDATE SUCCSSFULLY!!", "success"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		User user = userRepository.getUserByUserName(principal.getName());
		contact.setUser(user);
		contactRepository.save(contact);
		return "redirect:/user/" + contact.getcId() + "/contact";
	}

	/*
	 * The method is to display all the details of currently login user.
	 * */
	@GetMapping("/profile")
	public String profileHandler(Model model, Principal principal) {
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);
		model.addAttribute("user", user);
		return "user_profile";
	}
	
	

	/*
	 * This method is used to display change password form.
	 * */
	@GetMapping("/settings")
	public String changePassword() {
		return "change_password";
	}

	
	/*
	 * The method changePasswordHandler is used to change password of user account.
	 * It contain two one for old password and another is new password. If the input value entered by user for old password does not match with the password stored 
	 * in database then it will show error.
	 * If old password matches with password saved in Database, then user input new password value will be replaced in database.     
	 * */
	@PostMapping("/change-password")
	public String changePasswordHandler(@RequestParam String oldPassword, 
			@RequestParam String newPassword,
			Principal principal, HttpSession session) {

		String name = principal.getName();
		User currentUser = userRepository.getUserByUserName(name);		
		if(bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			
			currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
			userRepository.save(currentUser);
			session.setAttribute("message", new Message("PASSWORD CHANGED SUCCESSFULLY", "success"));
		}else {
			
			session.setAttribute("message", new Message("Please enter correct password", "danger"));
			return "redirect:/user/settings";
		}
		return "redirect:/user/index";
	}
	
	/*
	 * This method is used to redirect user details page to update all the details. 
	 * */
	@PostMapping("/update-user/{userid}")
	public String updateUserForm(@PathVariable("userid") Integer uid, Model model) {
		User user = userRepository.findById(uid).get();
		model.addAttribute("user", user);
		return "update-form-user";
	}
	
	/*
	 * this method is used to update the user details and change the corresponding value in database. 
	 * */
	@PostMapping("/process-update-user")
	public String updateUserHandler(@ModelAttribute User user, Model model,
			HttpSession session, Principal principal) {
		
		userRepository.save(user);
		return "redirect:/user/profile";
	}
	
}
