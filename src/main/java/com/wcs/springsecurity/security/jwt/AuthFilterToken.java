package com.wcs.springsecurity.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wcs.springsecurity.entity.User;
import com.wcs.springsecurity.security.service.UserDetailsServiceImpl;

public class AuthFilterToken extends OncePerRequestFilter {
	
	@Autowired
	JWTUtils jwtUtils;
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	/*
	 * Méthode qui est appellé quand un utilisateur appelle une ressource en étant connecté
	 * On doit donc verifié si un token est présent, et s'il est présent on va le valider
	 * Et placer l'utilisateur connecté à un endroit ou on peut le récupèrer tout au long du traitement de l'appel
	 * 
	 * Si aucun token n'est présent, aucun traitement n'est effectué.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// verifier si un token est présent
		String token = this.getTokenFromHeader(request); 
		
		try {
			// si le token n'est pas null & qu'il est valide 
			if(token != null && jwtUtils.isValidToken(token)) {
				
				// Décoder token && recupèrer le user
				String username = jwtUtils.getUsernameFromToken(token);
				
				// recupèrer le user dans la DB (plus besoin de UserDetails
				User user =  userDetailsServiceImpl.loadUserByUsername(username);
				
				// on enregistre notre user qque part pour pouvoir le recup potentiellement plus tard
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
						user, null, user.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// On donne a Spring le contexte de sécurité dans lequel s'executer
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		catch(Exception e) {
			System.out.println("Jwt filter has ended with issue : "+ e.getMessage());
		}
		
		filterChain.doFilter(request, response);
	}

	private String getTokenFromHeader(HttpServletRequest request) {
		
		String authorization = request.getHeader("Authorization");
		
		// Si une authorization est présente, qu'elle commence bien par Bearer, on renvoi la chaine de caractères qui suis.
		if(authorization != null && authorization.startsWith("Bearer ")) {
			return authorization.substring(7);
		}
		return null;
	}
}
