package com.example.demo.login.domain.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.example.demo.login.domain.model.User;
public class UserResultSetExtractor implements ResultSetExtractor<List<User>>{
	@Override
	public List<User> extractData(ResultSet rs) throws SQLException,
			DataAccessException{
		List<User> userList = new ArrayList<>();
		while(rs.next()) {
			User user = new User();
			user.setUserId(rs.getInt("user_id"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			user.setUserName(rs.getString("user_name"));
			user.setBirthday(rs.getObject("birthday",LocalDate.class));
			user.setAge(rs.getInt("age"));
			user.setRole(rs.getString("role"));
			
			userList.add(user);
		}
		if(userList.size()==0) {
			throw new EmptyResultDataAccessException(1);
		}
		return userList;
	}
}