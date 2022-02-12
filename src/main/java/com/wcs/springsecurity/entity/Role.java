package com.wcs.springsecurity.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

/*
 * Role représentera les rôles dont a besoin Spring Security et donc implémentera GrantedAuthority
 * Pour que Spring puisse les comprendre nativement.
 * 
 * Quand nous implementons GrantedAuthority, on nous demande d'avoir une méthode "getAuthority" qui va chercher le nom du rôle
 * On suppose donc que le nom du rôle est une propriété authority, on la nomme donc comme ça au lieu de "name"
 */
@Entity
public class Role implements GrantedAuthority {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String authority;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	

}
