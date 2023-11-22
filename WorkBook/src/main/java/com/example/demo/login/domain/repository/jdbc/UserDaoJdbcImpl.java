package com.example.demo.login.domain.repository.jdbc;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;


@Repository("UserDaoJdbcImpl")
public class UserDaoJdbcImpl implements UserDao{
	
	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	 // ユーザー数を取得する
    @Override
    public int count() throws DataAccessException {
        return jdbc.queryForObject("SELECT COUNT(*) FROM m_user", Integer.class);
    }
    
    // ユーザーを1件挿入する
    @Override
    public int insertOne(User user) throws DataAccessException {
    	// パスワードをハッシュ化
        String password = passwordEncoder.encode(user.getPassword());
		String sql = "INSERT INTO m_user(email,"
				+"password,"
				+"user_name,"
				+"birthday,"
				+"age,"
				+"role)"
				+"VALUES(?,?,?,?,?,?)";
		
		int rowNumber = jdbc.update(sql,
				user.getEmail(),
				password,
//				user.getPassword(),
				user.getUserName(),
				user.getBirthday(),
				user.getAge(),
				user.getRole());
		
		return rowNumber;
    }
 // ユーザーを1件取得する
    @Override
    public User selectOne(Integer userId) throws DataAccessException {
        try {
            Map<String, Object> map = jdbc.queryForMap("SELECT * FROM m_user WHERE user_id = ?", userId);
            return mapToUser(map);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    // メールアドレスからユーザーを1件取得する
    @Override
    public User selectEmailOne(String email) throws DataAccessException {
        try {
            Map<String, Object> map = jdbc.queryForMap("SELECT * FROM m_user WHERE email = ?", email);
            return mapToUser(map);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }	
    // 全てのユーザーを取得する
    @Override
    public List<User> selectMany() throws DataAccessException {
        List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM m_user");
        return getList.stream().map(this::mapToUser).collect(Collectors.toList());
    }
    // ユーザーを1件更新する
    @Override
    public int updateOne(User user) throws DataAccessException {
    	// パスワードをハッシュ化
        String password = passwordEncoder.encode(user.getPassword());
		String sql = "UPDATE m_user"
				+ " SET"
				+ " email=?,"
				+ " password=?,"
				+ " user_name=?,"
				+ " birthday=?,"
				+ " age=?"
				+ " WHERE user_id=?";
		int rowNumber = jdbc.update(sql,
				user.getEmail(),
				password,
//				user.getPassword(),
				user.getUserName(),
				user.getBirthday(),
				user.getAge(),
				user.getUserId());
		
		return rowNumber;
    }
    // ユーザーを1件削除する
    @Override
    public int deleteOne(Integer userId) throws DataAccessException {
        return jdbc.update("DELETE FROM m_user WHERE user_id=?", userId);
    }
    // ユーザー情報をCSVに出力する    
    @Override
    public void userCsvOut() throws DataAccessException {
        String sql = "SELECT * FROM m_user";
        UserRowCallbackHandler handler = new UserRowCallbackHandler();
        jdbc.query(sql, handler);
    }
    // MapからUserオブジェクトに変換する
    private User mapToUser(Map<String, Object> map) {
        User user = new User();
        user.setUserId((Integer) map.get("user_id"));
        user.setEmail((String) map.get("email"));
        user.setPassword((String) map.get("password"));
        user.setUserName((String) map.get("user_name"));
        user.setBirthday(((Date) map.get("birthday")).toLocalDate());
        user.setAge((Integer) map.get("age"));
        user.setRole((String) map.get("role"));
        return user;
    }
}