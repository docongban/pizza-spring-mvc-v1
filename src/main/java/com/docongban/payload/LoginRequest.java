package com.docongban.payload;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
	// Đánh dấu phone không được trống
	@NotBlank
	private String phone;
	
	// Đánh dấu password không được trống
	@NotBlank
	private String password;
	
}
