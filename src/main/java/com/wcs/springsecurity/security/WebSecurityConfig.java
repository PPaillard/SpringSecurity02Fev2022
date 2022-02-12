package com.wcs.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wcs.springsecurity.security.jwt.AuthFilterToken;
import com.wcs.springsecurity.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Bean
	public AuthFilterToken getAuthTokenFilter() {
		return new AuthFilterToken();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeHttpRequests()
			.antMatchers("/auth").permitAll()
			.antMatchers("/admin").hasRole("ROLE_ADMIN")
			.antMatchers("/articles").permitAll()
			.antMatchers("/users").authenticated();
		
		// On demande d'appliquer un filtre avant que chaque requête envoyée a l'API soit executer 
		// (Celle qui permet de savoir si un token est présent dans la requête et s'il est présent, d'auth le user)
		http.addFilterBefore(getAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	/*
	 * On indique à Spring quel service utiliser pour recupèrer son UserDetails (utilisateur avec les rôles etc qu'il pourra authentifier)
	 * On lui spécifie quel encodeur on veut utiliser (la méthode passwordEncode() renvoi un encodeur BCrypt)
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
