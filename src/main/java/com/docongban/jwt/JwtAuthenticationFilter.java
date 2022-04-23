package com.docongban.jwt;

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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.docongban.service.impl.AccountServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * Kiểm tra chuỗi JWT nhận từ request có hợp lệ hay không?
 * @author NPTAN
 * 
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private AccountServiceImpl accountServiceImpl;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			// Lấy jwt từ request
			String jwt = getJwtFromRequest(request);
			
			if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				// Lấy id user từ chuỗi jwt
				Integer accountId = tokenProvider.getAccountIdFromJWT(jwt);
				// Lấy thông tin người dùng từ id
				UserDetails userDetails = accountServiceImpl.loadUserById(accountId);
				if(userDetails != null) {
					// Nếu người dùng hợp lệ, set thông tin cho Security Context
					UsernamePasswordAuthenticationToken 
							authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
                    SecurityContextHolder.getContext().setAuthentication(authentication);
				}
				
			}
			
		} catch (Exception e) {
			log.error("failed on set user authentication", e);
		}
		
		filterChain.doFilter(request, response);
	}
	
	/*
	 * Lấy ra token từ request gửi lên
	 */
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		// Kiểm tra xem Header Authorization có chứa thông tin jwt hay không?
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
	
}
