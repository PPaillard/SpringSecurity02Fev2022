package com.wcs.springsecurity.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wcs.springsecurity.security.service.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWTUtils {

	@Value("${wcslyon.app.jwtExpirationMs}")
	private int expirationMs;
	
	@Value("${wcslyon.app.jwtSecret}")
	private String secretKey;
	
	public String generateToken(UserDetailsImpl userDetailsImpl) {
		
		// on construit le token grace au user name, date /h actuelle, date d'expiration & clé d'encodage
		return Jwts.builder().setSubject(userDetailsImpl.getUsername())
			.setIssuedAt(new Date())
			.setExpiration(new Date((new Date()).getTime() + expirationMs))
			.signWith(SignatureAlgorithm.HS512, secretKey)
			.compact();
	}
	
	// on récupère le user depuis le token
	public String getUsernameFromToken(String token) {
		return Jwts.parser().setSigningKey(secretKey)
				.parseClaimsJws(token).getBody().getSubject();
	}
	
	// on verifie si le token est valide en tentant de récupèrer les claims du token
	// En cas de pépin, on aura une exception
	public boolean isValidToken(String token) {
		
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			System.out.println("Invalid JWT signature:" + e.getMessage());
		} catch (MalformedJwtException e) {
			System.out.println("Invalid JWT token: "+e.getMessage());
		} catch (ExpiredJwtException e) {
			System.out.println("JWT token is expired: "+e.getMessage());
		} catch (UnsupportedJwtException e) {
			System.out.println("JWT token is unsupported: "+e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("JWT claims string is empty: "+e.getMessage());
		}

		return false;
	
	}
}
