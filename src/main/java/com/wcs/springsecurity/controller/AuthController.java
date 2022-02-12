package com.wcs.springsecurity.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wcs.springsecurity.dto.AuthResponseDto;
import com.wcs.springsecurity.dto.UserAuthDto;
import com.wcs.springsecurity.dto.UserDto;
import com.wcs.springsecurity.entity.ERole;
import com.wcs.springsecurity.entity.Role;
import com.wcs.springsecurity.entity.User;
import com.wcs.springsecurity.repository.RoleRepository;
import com.wcs.springsecurity.repository.UserRepository;
import com.wcs.springsecurity.security.jwt.JWTUtils;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JWTUtils jwtUtils;

	@PostMapping("/signup")
	public void registerUser(@Valid @RequestBody UserDto userDto) {
		// on verifie que l'utilisateur n'existe pas déjà dans la BDD
		if(userRepository.existsByUsername(userDto.getUsername())
				|| userRepository.existsByEmail(userDto.getEmail())){
			
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
		// Création de l'utilisateur avec les datas du DTO
		User user = new User();
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		// on utilise l'encodeur fournis par le bean dans le fichier WebSecurityConfig
		user.setPassword(passwordEncoder.encode(userDto.getPassword()) );
		
		// on va chercher le role user pour le mettre par defaut à chaque personne s'enregistrant
		Role role = roleRepository.findByAuthority(ERole.ROLE_USER.name())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		// on doit associer une liste de role à un utilisateur, donc on en créé une et on y ajoute le role USER
		List<Role> roles = new ArrayList<>();
		roles.add(role);
		user.setAuthorities(roles);
		// on save le user avec toutes les données précedentes
		userRepository.save(user);
	}
	
	@PostMapping("/signin")
	public AuthResponseDto authentUser(@Valid @RequestBody UserAuthDto userAuthDto) {
		
		// grace à l'authenticationManager, on peut verifier que le user/pdw correspond a qqchose d'existant et que le couple est correct.
		Authentication authentication =  authenticationManager.authenticate(
				 new UsernamePasswordAuthenticationToken(userAuthDto.getUsername(), userAuthDto.getPassword()));
		
		// Spring nous fournis l'utilisateur connecté dans l'objet authentication via
		// la méthode getPrincipal()
		// Spring gère plusieurs méthodes : nous devons donc caster le résultat en type UserDetailsImpl
		User user = (User) authentication.getPrincipal();
		
		return new AuthResponseDto(
				user.getUsername(),
				user.getAuthorities(),
				jwtUtils.generateToken(user));
	}
}
