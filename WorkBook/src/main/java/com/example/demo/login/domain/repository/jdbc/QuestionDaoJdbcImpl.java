package com.example.demo.login.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.Question;
import com.example.demo.login.domain.repository.QuestionDao;


@Repository("QuestionDaoJdbcImpl")
public class QuestionDaoJdbcImpl implements QuestionDao{
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Override	
	public int count() throws DataAccessException{
		int count = jdbc.queryForObject("SELECT COUNT(*) FROM m_question",Integer.class);
		return count;
	}
	@Override
	public int insertOne(Question question)throws DataAccessException{
		
		String sql = "INSERT INTO m_question(category,"
				+"questionStatement,"
				+"choice1,"
				+"choice2,"
				+"choice3,"
				+"choice4,"
				+"answer,"
				+"explanation,"
				+"answered,"
				+"result)"
				+"VALUES(?,?,?,?,?,?,?,?,?,?)";
		
		int rowNumber = jdbc.update(sql,
				question.getCategory(),
				question.getQuestionStatement(),
				question.getChoice1(),
				question.getChoice2(),
				question.getChoice3(),
				question.getChoice4(),
				question.getAnswer(),
				question.getExplanation(),
				question.isAnswered(),
				question.isResult());
		
		return rowNumber;
	}
	@Override
	public Question selectOne(Integer questionId) throws DataAccessException{
		Map<String,Object> map = jdbc.queryForMap("SELECT * FROM m_question"+" WHERE question_id = ?",questionId);
		Question question = new Question();
		
		question.setQuestionId((Integer)map.get("question_id"));
		question.setCategory((String)map.get("category"));		
		question.setQuestionStatement((String)map.get("question_statement"));
		question.setChoice1((String)map.get("choice1"));
		question.setChoice2((String)map.get("choice2"));
		question.setChoice3((String)map.get("choice3"));
		question.setChoice4((String)map.get("choice4"));
		question.setAnswer((Integer)map.get("answer"));
		question.setExplanation((String)map.get("explanation"));
		question.setAnswered((Boolean)map.get("answered"));
		question.setResult((Boolean)map.get("result"));
		
		return question;
		
	}
	@Override
	public List<Question> selectMany() throws DataAccessException{
			List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM m_question");
			List<Question> questionList = new ArrayList<>();
			for(Map<String, Object> map: getList) {
				Question question = new Question();
				
				question.setQuestionId((Integer)map.get("question_id"));
				question.setCategory((String)map.get("category"));		
				question.setQuestionStatement((String)map.get("question_statement"));
				question.setChoice1((String)map.get("choice1"));
				question.setChoice2((String)map.get("choice2"));
				question.setChoice3((String)map.get("choice3"));
				question.setChoice4((String)map.get("choice4"));
				question.setAnswer((Integer)map.get("answer"));
				question.setExplanation((String)map.get("explanation"));
				question.setAnswered((Boolean)map.get("answered"));
				question.setResult((Boolean)map.get("result"));
				
				questionList.add(question);
			}
			return questionList;
	}
	@Override
	public int updateOne(Question question) throws DataAccessException{
		
		
		String sql = "UPDATE m_question"
				+ " SET"
				+ " category=?,"
				+ " questionStatement=?,"
				+ " choice1=?,"
				+ " choice2=?,"
				+ " choice3=?,"
				+ " choice4=?,"
				+ " answer=?,"
				+ " explanation=?,"
				+ " answered=?"
				+ " WHERE question_id=?";
		
		int rowNumber = jdbc.update(sql,
			question.getCategory(),
			question.getQuestionStatement(),
			question.getChoice1(),
			question.getChoice2(),
			question.getChoice3(),
			question.getChoice4(),
			question.getAnswer(),
			question.getExplanation(),
			question.isAnswered(),
			question.isResult());
		return rowNumber;
		
	}
	@Override
	public int deleteOne(Integer questionId)throws DataAccessException{
		int rowNumber = jdbc.update("DELETE FROM m_question WHERE question_id=?",questionId);
			return rowNumber;
	}
	@Override
	public void questionCsvOut() throws DataAccessException{
		String sql = "SELECT * FROM m_question";
		QuestionRowCallbackHandler handler = new QuestionRowCallbackHandler();
		jdbc.query(sql, handler);
	}
	@Override
	public List<String> groupByCategory() throws DataAccessException{
		String sql = "SELECT category FROM m_question GROUP BY category";
		List<Map<String,Object>> getList = jdbc.queryForList(sql);
		List<String> categoryList = new ArrayList<>();
		for (Map<String,Object> map : getList) {
			String category = (String)map.get("category");
			categoryList.add(category);
		}
		return categoryList;
		
	}
}