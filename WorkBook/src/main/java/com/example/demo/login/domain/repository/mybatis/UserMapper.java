package com.example.demo.login.domain.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.login.domain.model.User;

@Mapper
public interface UserMapper{
	
	
	
	
	public boolean isEmailAlreadyRegistered(User user);
	
	@Insert("INSERT INTO m_user ("
			+ " email,"
			+ " password,"
			+ " user_name,"
			+ " birthday,"
			+ " age,"
			+ " role)"
			+ " VALUES ("
			+ " #{email},"
			+ " #{password},"
			+ " #{userName},"
			+ " #{age},"
			+ " #{role})")
	public boolean insert(User user);
	
	@Select("SELECT user_id AS userId,"
			+ "email,"
			+ "password,"
			+ "user_name AS userName,"
			+ "birthday,"
			+ "age,"
			+ "role"
			+ " FROM m_user"
			+ " WHERE user_id = #{userId}")
	public User selectOne(Integer userId);
	
	@Select("SELECT user_id AS userId,"
			+ "email,"
			+ "password,"
			+ "user_name AS userName,"
			+ "birthday,"
			+ "age,"
			+ "role"
			+ " FROM m_user"
			+ " WHERE email = #{email}")
	public User selectEmailOne(String email);
	
	@Select("SELECT user_id AS userId,"
			+"email,"
			+"password,"
			+"user_name AS userName,"
			+"birthday,"
			+"age,"
			+"role"
			+" FROM m_user")
	public List<User> selectMany();
	
	@Update("UPDATE m_user SET"
			+" email = #{email},"
			+" password = #{password},"
			+" user_name = #{userName},"
			+" birthday = #{birthday},"
			+" age = #{age},"
			+" WHERE user_id = #{userId}")
	public boolean updateOne(User user);
	
	@Delete("DELETE FROM m_user WHERE user_id = #{userId}")
	public boolean deleteOne(Integer userId);
		
}