package com.example.demo.login.domain.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.example.demo.login.domain.model.User;

public interface UserDao {
	public int count() throws DataAccessException;
	public int insertOne(User user) throws DataAccessException;
	public User selectOne(Integer userId) throws DataAccessException;	
	public User selectEmailOne(String email) throws DataAccessException;
	public List<User> selectMany() throws DataAccessException;
	public int updateOne(User user) throws DataAccessException;
	public int deleteOne(Integer userId) throws DataAccessException;
	public void userCsvOut() throws DataAccessException;
	
}