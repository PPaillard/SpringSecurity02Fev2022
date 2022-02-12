package com.wcs.springsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wcs.springsecurity.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	/*
	 * Comme le nom est devenu une propriété authority, on doit modifier le findByName par findByAuthority
	 */
	Optional<Role> findByAuthority(String authority);
}
