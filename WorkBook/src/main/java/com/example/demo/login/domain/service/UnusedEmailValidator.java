	package com.example.demo.login.domain.service;
	
	import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
	
	public class UnusedEmailValidator implements ConstraintValidator<UnusedEmail, String> {
	
	    private final UserDao userDao;
	    private String[] applyToPages; // 適用するページのリスト
	
	    public UnusedEmailValidator(@Qualifier("UserDaoJdbcImpl2") UserDao userDao) {
	        this.userDao = userDao;
	    }
	
	    @Override
	    public void initialize(UnusedEmail constraintAnnotation) {
	    }
	
	    @Override
	    public boolean isValid(String value, ConstraintValidatorContext context) {
	    	if (applyToPages.length > 0 && Arrays.asList(applyToPages).contains(getCurrentPage())) {
	    		User user = userDao.selectEmailOne(value);
	    		return user == null;
	        }
	    	// ページが指定されていない場合は常に true を返す（バリデーション無効）
	        return true;
	    }
	    private String getCurrentPage() {
	        // HttpServletRequestを使用して現在のURIを取得
	        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
	        // リクエストURIが "/login" の場合、"login" を返す
	        if (uri.equals("/login")) {
	            return "login";
	        }
	        // 他の場合は空の文字列を返す
	        return "";
	    }


	}
