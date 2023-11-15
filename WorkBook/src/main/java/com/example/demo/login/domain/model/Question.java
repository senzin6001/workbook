package com.example.demo.login.domain.model;


import lombok.Data;

@Data
public class Question {

	
	private int questionId;

	private String category;
	
	private String questionStatement;
	
	private String choice1;
	
	private String choice2;
	
	private String choice3;
	
	private String choice4;
	
	private int answer;
	
	private String explanation;

	private boolean answered;
	
	private boolean result;
	
}
