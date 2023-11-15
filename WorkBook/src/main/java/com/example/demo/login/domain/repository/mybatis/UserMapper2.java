package com.example.demo.login.domain.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.login.domain.model.User;

@Mapper
public interface UserMapper2{
	

	public boolean insert(User user);
	
	public User selectOne(Integer userId);
	
	public User selectEmailOne(String Email);
	
	public List<User> selectMany();
	
	public boolean updateOne(User user);
	
	public boolean deleteOne(Integer userId);
		
}