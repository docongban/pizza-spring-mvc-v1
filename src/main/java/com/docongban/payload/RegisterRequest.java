package com.docongban.payload;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {
	// Đánh dấu phone không được trống
	@NotBlank
	private String phone;
	
	// Đánh dấu password không được trống
	@NotBlank
	private String password;
	private String fullname;
	private String email;
	private String address;
	private String role;
	
}
