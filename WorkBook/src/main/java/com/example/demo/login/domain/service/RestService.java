package com.example.demo.login.domain.service;


import java.util.List;

import com.example.demo.login.domain.model.User;

public interface RestService{
	public boolean isEmailAlreadyRegistered(User user);
	
	public boolean insert(User user);
	
	public User selectOne(Integer userId);
	
	public List<User> selectMany();
	
	public boolean update(User user);
	
	public boolean delete(Integer userId);
}