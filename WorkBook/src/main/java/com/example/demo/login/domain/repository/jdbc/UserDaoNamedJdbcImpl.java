package com.example.demo.login.domain.repository.jdbc;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;

@Repository("UserDaoNamedJdbcImpl")
public class UserDaoNamedJdbcImpl implements UserDao{
	@Autowired
	private NamedParameterJdbcTemplate jdbc;
	@Override
	public int count() {
		String sql = "SELECT COUNT(*) FROM m_user";
		SqlParameterSource params = new MapSqlParameterSource();
		return jdbc.queryForObject(sql, params, Integer.class);
	}
	@Override
	public int insertOne(User user){
		String sql = "INSERT INTO m_user(email,"				
				+" password,"
				+" user_name,"
				+" birthday,"
				+" age,"
				+" role)"
				+" VALUES(:email,"
				+" :password,"
				+" :userName,"
				+" :birthday,"
				+" :age,"
				+" :role)";
		SqlParameterSource params = new MapSqlParameterSource()
			.addValue("email",user.getEmail())
			.addValue("password",user.getPassword())
			.addValue("userName",user.getUserName())
			.addValue("birthday",user.getBirthday())
			.addValue("age",user.getAge())
			.addValue("role",user.getRole());
		return jdbc.update(sql, params);
				
	}
	public User selectOne(Integer userId) {
		String sql = "SELECT * FROM m_user WHERE user_id = :userId";
		SqlParameterSource params = new MapSqlParameterSource()
			.addValue("userId", userId);
		Map<String,Object> map = jdbc.queryForMap(sql,params);
		User user = new User();
		user.setUserId((Integer)map.get("user_id"));		
		user.setEmail((String)map.get("email"));
		user.setPassword((String)map.get("password"));
		user.setUserName((String)map.get("user_name"));
		user.setBirthday(((Date) map.get("birthday")).toLocalDate());
		user.setAge((Integer)map.get("age"));
		user.setRole((String)map.get("role"));
		return user;
		
	}
	public User selectEmailOne(String email) {
		try {
			String sql = "SELECT * FROM m_user WHERE email = :email";
			SqlParameterSource params = new MapSqlParameterSource()
				.addValue("email", email);
			Map<String,Object> map = jdbc.queryForMap(sql,params);
			User user = new User();
			user.setUserId((Integer)map.get("user_id"));		
			user.setEmail((String)map.get("email"));
			user.setPassword((String)map.get("password"));
			user.setUserName((String)map.get("user_name"));
			user.setBirthday(((Date) map.get("birthday")).toLocalDate());
			user.setAge((Integer)map.get("age"));
			user.setRole((String)map.get("role"));
			return user;
	    } catch (EmptyResultDataAccessException e) {
	    	
	        return null;
	    }
		
	}
	public List<User> selectMany(){
		String sql = "SELECT * FROM m_user";
		SqlParameterSource params =new MapSqlParameterSource();
		List<Map<String,Object>> getList = jdbc.queryForList(sql, params);
		List<User> userList = new ArrayList<>();
		for(Map<String,Object> map: getList) {
			User user = new User();
			user.setUserId((Integer)map.get("user_id"));
			user.setEmail((String)map.get("email"));
			user.setPassword((String)map.get("password"));
			user.setUserName((String)map.get("user_name"));
			user.setBirthday(((Date) map.get("birthday")).toLocalDate());
			user.setAge((Integer)map.get("age"));
			user.setRole((String)map.get("role"));
			
			userList.add(user);
			
		}
		return userList;
	}
	
	@Override
	public int updateOne(User user) {
		String sql = "UPDATE M_USER"
				+ " SET"
				+ " email = :email,"
				+ " password = :password,"
				+ " user_name = :userName,"
				+ " birthday = :birthday,"
				+ " age = :age"
				+ " WHERE user_id = :userId";
		SqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId",user.getUserId())
				.addValue("email",user.getEmail())
				.addValue("password",user.getPassword())
				.addValue("userName",user.getUserName())
				.addValue("birthday",user.getBirthday())
				.addValue("age",user.getAge());
			return jdbc.update(sql, params);
				
	}
	@Override
	public int deleteOne(Integer userId) {
		String sql = "DELETE FROM m_user WHERE user_id = :userId";
		SqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId", userId);
		int rowNumber = jdbc.update(sql, params);
		return rowNumber;
	}
	@Override
	public void userCsvOut() {
		String sql = "SELECT * FROM m_user";
		UserRowCallbackHandler handler = new UserRowCallbackHandler();
		jdbc.query(sql, handler);
	}	

}

