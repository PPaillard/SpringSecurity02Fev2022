package com.wcs.springsecurity.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wcs.springsecurity.entity.Role;
import com.wcs.springsecurity.entity.User;

public class UserDetailsImpl implements UserDetails {
	
	private Long id;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private List<SimpleGrantedAuthority> authorities;
	
	public static UserDetailsImpl build(User user) {
		UserDetailsImpl userDetailsImpl = new UserDetailsImpl();
		userDetailsImpl.setId(user.getId());
		userDetailsImpl.setUsername(user.getUsername());
		userDetailsImpl.setPassword(user.getPassword());
		userDetailsImpl.setEmail(user.getEmail());
		
		// On convertis la liste de rôles contenue dans notre entity User vers 
		// une liste de SimpleGrantedAuythority
		List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
		for(Role role : user.getRoles()) {
			simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getName().name()));
		}
		userDetailsImpl.setAuthorities(simpleGrantedAuthorities);
		return userDetailsImpl;
	}

	@Override
	public List<SimpleGrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
		this.authorities = authorities;
	}
}
