package com.wcs.springsecurity.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.userdetails.UserDetails;

@Entity
/*
 * On utilise l'annotation @Table pour spécifier le nom de la table dans la BDD
 * On indique également les contraintes d'unicité : 
 * 	- Un username ne peut être présent qu'UNE SEULE FOIS, si on cherche à le mettre 2 fois => exception SQL
 * 	- Un email ne peut être présent qu'UNE SEULE FOIS, si on cherche à le mettre 2 fois => exception SQL
 */
@Table(	name = "user", 
uniqueConstraints = { 
		@UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") 
	})
/*
 * Plutôt que de créer une autre class UserDetailsImpl, on l'implémente directement dans notre ENTITY
 * Comme nos rôles sont utilisés en tant que "autorities" par Spring, on va les nommer comme ça.
 * Pas besoin de méthode static BUILD étant donné que notre entity USER est EGALEMENT un UserDetails (l'implémente)
 * 
 * On pourrait aussi utiliser les méthodes natives de UserDetails pour bloquer un compte, désactiver un compte, etc
 * Mais on ne le fera pas ici : ces méthodes retourneront "true" par defaut (compte non bloqué, non désactivé, etc)
 */
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	
	private String password;
	
	private String email;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> authorities;
	
	
	public void setAuthorities(List<Role> authorities) {
		this.authorities = authorities;
	}

	@Override
	public List<Role> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authorities;
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
}
