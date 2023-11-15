package com.example.demo.login.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.login.domain.model.GroupOrder;
import com.example.demo.login.domain.model.SignupForm;
import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.service.UserService;




@Controller
public class SignupController{
	@Autowired
	private UserService userService;

	private Map<String,String> radioMarriage;
	
	private Map<String,String> initRadioMarriage(){
		Map<String, String> radio = new LinkedHashMap<>();
		
		radio.put("既婚", "true");
		radio.put("未婚", "false");
		
		return radio;
	}
	
	private User convertSignupFormToUser(SignupForm form) {
	    User user = new User();
	    user.setEmail(form.getEmail());
	    user.setPassword(form.getPassword());
	    user.setUserName(form.getUserName());
	    user.setBirthday(form.getBirthday());
	    user.setAge(form.getAge());
	    user.setMarriage(form.isMarriage());
	    user.setRole("ROLE_GENERAL");
	    return user;
	}
	@GetMapping("/signup")
	public String getSignUp(@ModelAttribute SignupForm form,Model model) {
		radioMarriage = initRadioMarriage();
		
		model.addAttribute("radioMarriage",radioMarriage);
		return "login/signup";
		
	}

	@PostMapping("/signup")
	public String postSignUp(@ModelAttribute @Validated(GroupOrder.class) SignupForm form,
			BindingResult bindingResult,
			Model model) {
		
			if(bindingResult.hasErrors()) {	
				return getSignUp(form,model);
			}

			System.out.println(form);
			
			User user = convertSignupFormToUser(form);
			
			if(userService.isEmailAlreadyRegistered(user)) {
				// エラーメッセージをモデルに追加
		        model.addAttribute("errorMessage", "メールアドレスが重複しています。");
		        return getSignUp(form,model);
				
			}

			boolean result = userService.insert(user);
			
			if(result == true) {
				System.out.println("insert成功");
			} else {
				System.out.println("insert失敗");
			}
			
			return "redirect:/login";
	}
	@ExceptionHandler(DataAccessException.class)
	public String dataAccessExceptionHandler(DataAccessException e,Model model) {
		model.addAttribute("error","内部サーバーエラー (DB) : ExceptionHandler");
		model.addAttribute("message","SignupControllerでDataAccessExceptionが発生しました");
		model.addAttribute("status",HttpStatus.INTERNAL_SERVER_ERROR);
		return "error";
		
	}
	@ExceptionHandler(Exception.class)	
	public String exceptionHandler(Exception e,Model model) {
		model.addAttribute("error","内部サーバーエラー (DB) : ExceptionHandler");
		model.addAttribute("message","SignupControllerでExceptionが発生しました");
		model.addAttribute("status",HttpStatus.INTERNAL_SERVER_ERROR);
		return "error";
		
	}	
	
}