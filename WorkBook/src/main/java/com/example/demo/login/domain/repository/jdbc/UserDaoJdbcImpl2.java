package com.example.demo.login.domain.repository.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;

@Repository("UserDaoJdbcImpl2")
public class UserDaoJdbcImpl2 extends UserDaoJdbcImpl{
	@Autowired
	private JdbcTemplate jdbc;
	@Override
	public User selectOne(Integer userId) {
		String sql = "SELECT * FROM m_user WHERE user_id = ?";
		UserRowMapper rowMapper = new UserRowMapper();
		return jdbc.queryForObject(sql,rowMapper,userId);
	}
	@Override
	public User selectEmailOne(String email) throws DataAccessException {
	    try {
			String sql = "SELECT * FROM m_user WHERE email = ?";
	        
			UserRowMapper rowMapper = new UserRowMapper();
			
			return jdbc.queryForObject(sql,rowMapper,email);
			
	    } catch (EmptyResultDataAccessException e) {
	    	
	        return null; 
	    }
	}

	@Override
	public List<User>selectMany(){
		String sql ="SELECT * FROM m_user";
		RowMapper<User> rowMapper= new UserRowMapper();
		return jdbc.query(sql, rowMapper);
	}
}