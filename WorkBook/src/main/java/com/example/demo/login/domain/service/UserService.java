package com.example.demo.login.domain.service;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;

@Transactional
@Service
public class UserService{
	
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
	
	public boolean insert(User user) {
		
	    int rowNumber = dao.insertOne(user);
	    
	    return rowNumber > 0;
	}
	
	public int count() {
		return dao.count();	
	}
	
	public User selectOne(Integer userId) {
		return dao.selectOne(userId);
	}
	
	public List<User> selectMany(){
		return dao.selectMany();
	}

	public boolean updateOne(User user) {
		int rowNumber = dao.updateOne(user);
		boolean result = false;
		if(rowNumber >0) {
			result = true;
			return result;
		}
		return result;
	}
	public boolean deleteOne(Integer userId) {
		int rowNumber = dao.deleteOne(userId);
		boolean result = false;
		if(rowNumber > 0){
			result = true;
		}
		return result;
	}
	public void userCsvOut() throws DataAccessException{
		dao.userCsvOut();
	}
	public byte[] getFile(String fileName) throws IOException{
		FileSystem fs = FileSystems.getDefault();
		Path p = fs.getPath(fileName);
		byte[] bytes = Files.readAllBytes(p);
		return bytes;
	}
	

}
