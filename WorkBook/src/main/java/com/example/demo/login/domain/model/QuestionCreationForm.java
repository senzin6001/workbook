package com.example.demo.login.domain.model;


import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionCreationForm {


		private int questionId;
		@NotBlank(groups = ValidGroup1.class)
		@Length(max=30,groups = ValidGroup2.class)
		private String category;
		
		@NotBlank(groups = ValidGroup1.class)
		@Length(max=256,groups = ValidGroup2.class)
		private String questionStatement;
		
		@NotBlank(groups = ValidGroup1.class)
		@Length(max=50,groups = ValidGroup2.class)
		private String choice1;
		
		@NotBlank(groups = ValidGroup1.class)
		@Length(max=50,groups = ValidGroup2.class)
		private String choice2;
		
		@NotBlank(groups = ValidGroup1.class)
		@Length(max=50,groups = ValidGroup2.class)
		private String choice3;
		
		@NotBlank(groups = ValidGroup1.class)
		@Length(max=50,groups = ValidGroup2.class)
		private String choice4;
		
		@Min(value=1,groups = ValidGroup2.class)
		@Max(value=4,groups = ValidGroup2.class)
		@NotNull(groups = ValidGroup1.class)
		private int answer;
		
		@NotBlank(groups = ValidGroup1.class)
		@Length(max=256,groups = ValidGroup2.class)		
		private String explanation;

		private boolean answered;
		
		private boolean result;
}
