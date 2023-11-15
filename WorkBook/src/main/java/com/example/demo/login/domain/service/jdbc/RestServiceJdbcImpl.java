package com.example.demo.login.domain.service.jdbc;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;
import com.example.demo.login.domain.service.RestService;

@Transactional
@Service
public class RestServiceJdbcImpl implements RestService{
	@Autowired
	@Qualifier("UserDaoJdbcImpl")
	UserDao dao;
	
	public boolean isEmailAlreadyRegistered(User user) {
	    // メールアドレスが既に登録されているかどうかの判定
		User existingUser = dao.selectEmailOne(user.getEmail());
		if (existingUser != null) {
	        return true;
	    }
		return false;
	}
	@Override
	public boolean insert(User user) {
		int result = dao.insertOne(user);
		
		if(result == 0) {
			return false;	
		}else {
			return true;
		}
	}
	@Override	
	public User selectOne(Integer userId) {
		return dao.selectOne(userId);
	}
	@Override	
	public List<User> selectMany(){
		return dao.selectMany();
	}
	@Override	
	public boolean update(User user) {
		int result = dao.updateOne(user);
		if(result == 0) {
			return  false;
		}else {
			return true;
		}
	}
	@Override	
	public boolean delete(Integer userId) {
		
		int result = dao.deleteOne(userId);
		
		if(result == 0) {
			return false;
		}else {
			return true;			
		}
	}
}
