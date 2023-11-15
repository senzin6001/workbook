package com.example.demo.login.domain.service.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.mybatis.UserMapper;
import com.example.demo.login.domain.service.RestService;


@Transactional
@Service("RestServiceMybatisImpl")
public class RestServiceMybatisImpl implements RestService{
	

  private final UserMapper userMapper;


    public RestServiceMybatisImpl(UserMapper userMapper, SqlSessionFactory sqlSessionFactory) {
        this.userMapper = userMapper;
        // 使用するために SqlSessionFactory を保存できます
    }
    
	@Override
	public boolean isEmailAlreadyRegistered(User user) {
		return userMapper.isEmailAlreadyRegistered(user);
	}   
	@Override
	public boolean insert(User user) {
		return userMapper.insert(user);
	}
	
	@Override
	public User selectOne(Integer userId) {
		return userMapper.selectOne(userId);
	}
	@Override
	public List<User> selectMany(){
		return userMapper.selectMany();
	}
	@Override
	public boolean update(User user) {
		return userMapper.updateOne(user);
	}
	@Override
	public boolean delete(Integer userId) {
		return userMapper.deleteOne(userId);
	}
}
