package com.wcs.springsecurity.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wcs.springsecurity.entity.User;
import com.wcs.springsecurity.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		/*
		 * On est toujours obligé d'utiliser la méthode findByUsername pour récupèrer le user
		 * Ce coup ci, comme User étends UserDetails, on peut le renvoyer directement s'il est trouvé.
		 */
		return userRepository.findByUsername(username).orElseThrow(
				()-> new UsernameNotFoundException("User not found"));
	}

}
