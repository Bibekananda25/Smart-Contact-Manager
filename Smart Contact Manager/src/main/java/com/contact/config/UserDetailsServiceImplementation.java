/*
 * The class UserDetailsServiceImplementation is implemented for UserDetailsService and its method.
 * */

package com.contact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contact.dao.UserRepository;
import com.contact.entites.User;

public class UserDetailsServiceImplementation implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	/*
	 * this method is checked the user is null or not null.If the user is not null it returns user email and password of the currently login user for login authentication.
	 * */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.getUserByUserName(username);
		if(user==null) {
			throw new UsernameNotFoundException("Could not found user !!");
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		return customUserDetails;
	}

}
