package com.example.demo.login.domain.repository.jdbc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;

@Repository("UserDaoNamedJdbcImpl2")
public class UserDaoNamedJdbcImpl2 extends UserDaoNamedJdbcImpl{

	@Autowired
	private NamedParameterJdbcTemplate jdbc;
	@Override
	public User selectEmailOne(String email) {
		try {
			String sql = "SELECT * FROM m_user WHERE email = :email";
			SqlParameterSource params = new MapSqlParameterSource().addValue("email",email);
			
			RowMapper<User> rowMapper = new UserRowMapper();
			return jdbc.queryForObject(sql,params,rowMapper);
	    } catch (EmptyResultDataAccessException e) {
	    	
	        return null; 
	    }
	}
}