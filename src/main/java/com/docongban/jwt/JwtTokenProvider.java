package com.docongban.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.docongban.custom.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

	// Đoạn mã JWT_SECRET
	private final String JWT_SECRET = "nhom10";

	// Thời gian hiệu lực của mỗi chuỗi jwt
	private final long JWT_EXPIRATION = 604800000L;

	// Tạo ra jwt từ thông tin user account
	public String generateToken(CustomUserDetails userDetails) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
		// Tạo chuỗi jwt từ id của user
		return Jwts.builder()
					.setSubject(Integer.toString(userDetails.getAccount().getId()))
					.setIssuedAt(now)
					.setExpiration(expiryDate)
					.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
					.compact();
	}
	
	// Lấy thông tin user account từ jwt
	public Integer getAccountIdFromJWT(String token) {
		Claims claims = Jwts.parser()
							.setSigningKey(JWT_SECRET)
							.parseClaimsJws(token)
							.getBody();
		
		return Integer.parseInt(claims.getSubject());
	}
	
	// Validate token
	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
			return true;
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty.");
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token.");
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token.");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token.");
		}
		return false;
	}

}
