package com.wcs.springsecurity.dto;

import java.util.List;

import com.wcs.springsecurity.entity.Role;

public class AuthResponseDto {
	private String username;
	private List<Role> roles;
	private String token;
	
	public AuthResponseDto(String username, List<Role> roles, String token) {
		this.username = username;
		this.roles = roles;
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
