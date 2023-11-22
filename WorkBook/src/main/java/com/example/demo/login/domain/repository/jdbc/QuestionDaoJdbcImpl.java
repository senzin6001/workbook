package com.example.demo.login.domain.repository.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.login.domain.model.Question;
import com.example.demo.login.domain.repository.QuestionDao;


@Repository("QuestionDaoJdbcImpl")
public class QuestionDaoJdbcImpl implements QuestionDao{
	
    private final JdbcTemplate jdbc;
    public QuestionDaoJdbcImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
	
	@Override	
	public int count() throws DataAccessException{
		int count = jdbc.queryForObject("SELECT COUNT(*) FROM m_question",Integer.class);
		return count;
	}
	@Override
	public int insertOne(Question question)throws DataAccessException{
		
		String sql = "INSERT INTO m_question(category,"
				+"question_statement,"
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
		System.out.println(question.getCategory());
		System.out.println(question.getQuestionStatement());
		System.out.println(question.getChoice1());
		System.out.println(question.getChoice2());
		System.out.println(question.getChoice3());
		System.out.println(question.getChoice4());
		System.out.println(question.getAnswer());
		System.out.println(question.getExplanation());
		System.out.println(question.isAnswered());
		System.out.println(question.isResult());
		System.out.println(question.getQuestionId());
		
		String sql = "UPDATE m_question"
				+ " SET"
				+ " category=?,"
				+ " question_statement=?,"
				+ " choice1=?,"
				+ " choice2=?,"
				+ " choice3=?,"
				+ " choice4=?,"
				+ " answer=?,"
				+ " explanation=?,"
				+ " answered=?,"
				+ " result=?"
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
				question.isResult(),
				question.getQuestionId());
		System.out.println(sql);
		System.out.println(rowNumber);
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
	@Override	
	public void initQuestionTable() throws DataAccessException{		
		String sql = "DELETE FROM my_question";
		jdbc.update(sql);
	}
	
	@Override
	public void insertQuestionList(List<Question> questionList) throws DataAccessException {
	    // SQLクエリの作成
	    String inClause = questionList.stream()
	            .map(question -> String.valueOf(question.getQuestionId()))
	            .collect(Collectors.joining(","));
	    String sqlSelect = "SELECT * FROM m_question WHERE question_id IN (" + inClause + ")";

	    // m_questionテーブルから指定されたquestionListに含まれるquestionIdに一致するレコードを取得
	    List<Map<String, Object>> selectedRecords = jdbc.queryForList(sqlSelect);

	    // my_questionテーブルにレコードを挿入
	    String sqlInsert = "INSERT INTO my_question(question_id, category, question_statement, choice1, choice2, choice3, choice4, answer, explanation, answered, result)"
	            + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";

	    for (Map<String, Object> record : selectedRecords) {
	        jdbc.update(sqlInsert,
	                record.get("question_id"),
	                record.get("category"),
	                record.get("question_statement"),
	                record.get("choice1"),
	                record.get("choice2"),
	                record.get("choice3"),
	                record.get("choice4"),
	                record.get("answer"),
	                record.get("explanation"),
	                record.get("answered"),
	                false);
	    }
	}
	@Override
	public List<Question> filterUnansweredQuestions() throws DataAccessException{		
        String sqlSelect = "SELECT * FROM my_question WHERE answered = false";

        List<Question> unansweredQuestions = jdbc.query(sqlSelect, (ResultSet rs, int rowNum) -> {
            Question question = new Question();
            question.setQuestionId(rs.getInt("question_id"));
            question.setCategory(rs.getString("category"));
            question.setQuestionStatement(rs.getString("question_statement"));
            question.setChoice1(rs.getString("choice1"));
            question.setChoice2(rs.getString("choice2"));
            question.setChoice3(rs.getString("choice3"));
            question.setChoice4(rs.getString("choice4"));
            question.setAnswer(rs.getInt("answer"));
            question.setExplanation(rs.getString("explanation"));
            question.setAnswered(rs.getBoolean("answered"));
            question.setResult(rs.getBoolean("result"));

            return question;
        });

        return unansweredQuestions;
		
		
	}
	
	@Override	
	public int myQuestionCount() throws DataAccessException{
		int count = jdbc.queryForObject("SELECT COUNT(*) FROM my_question",Integer.class);
		return count;
	}
	
    @Override
    public void saveResult(int questionId, boolean result) throws DataAccessException {
        // my_questionテーブルのresultフィールドを更新
        String sqlUpdateMyQuestion = "UPDATE my_question SET result = ? WHERE question_id = ?";
        jdbc.update(sqlUpdateMyQuestion, result, questionId);

        // m_questionテーブルのresultフィールドを更新
        String sqlUpdateMQuestion = "UPDATE m_question SET result = ? WHERE question_id = ?";
        jdbc.update(sqlUpdateMQuestion, result, questionId);
    }
    @Override
    public void saveAnswered(int questionId) throws DataAccessException {
        String sql = "UPDATE my_question SET answered = true WHERE question_id = ?";
        jdbc.update(sql,questionId);
    }
    
    @Override
    public int countCorrectAnswer() throws DataAccessException{
        String sql = "SELECT COUNT(*) FROM my_question WHERE result = true";
        return jdbc.queryForObject(sql, Integer.class);
    	
    }
    
    @Override
    public String uploadQuestionFile(MultipartFile file) throws DataAccessException{
    	try (BufferedReader br = new BufferedReader(new InputStreamReader(uploadFile.getInputStream(), StandardCharsets.UTF_8))){
    	      String line;
    	      while ((line = br.readLine()) != null) {
    	        final String[] split = line.split(",");
    	        Question question = new Question();
                question.setCategory(split[1]);
                question.setQuestionStatement(split[2]);
                question.setChoice1(split[3]);
                question.setChoice2(split[4]);
                question.setChoice3(split[5]);
                question.setChoice4(split[6]);
                question.setAnswer(Integer.parseInt(split[6]));
                question.setExplanation(split[7]);
                question.setAnswered(split[8]);
                question.setResult(split[9]);
    	      }
    	    } catch (IOException e) {
    	      throw new RuntimeException("ファイルが読み込めません", e);
    	    }

    	    return "redirect:/";
    	  }

}