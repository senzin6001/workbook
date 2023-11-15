package com.example.demo.login.domain.model;



import java.time.LocalDate;

import lombok.Data;

@Data
public class User{
	
	private int userId; 
	private String email;
	private String password;
	private String userName;
	private LocalDate birthday; 
	private int age;
	private boolean marriage; 
	private String role;
}