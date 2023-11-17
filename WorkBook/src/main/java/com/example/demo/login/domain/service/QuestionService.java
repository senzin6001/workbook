package com.example.demo.login.domain.service;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.login.domain.model.Question;
import com.example.demo.login.domain.repository.QuestionDao;

@Transactional
@Service
public class QuestionService{
	
	@Autowired
	@Qualifier("QuestionDaoJdbcImpl")
	QuestionDao dao;
	
	
	
    private List<Question> shuffleList(List<Question> questionList) {
        List<Question> shuffledList = new ArrayList<>(questionList);
        Collections.shuffle(shuffledList);
        return shuffledList;
    }
    
    private List<Question> extractQuestions(List<Question> questionList, int count) {
        List<Question> result = new ArrayList<>();

        if (questionList == null ||  count <= 0) {
            return result; // 引数が不正な場合は空のリストを返す
        }

        // 指定された数だけ抽出
        for (int i = 0; i < count && i < questionList.size(); i++) {
            result.add(questionList.get(i));
        }

        return result;
    }
	
	
	public boolean insert(Question user) {
		

	    int rowNumber = dao.insertOne(user);
	    
	    return rowNumber > 0;
	}
	
	public int count() {
		return dao.count();	
	}
	
	public Question selectOne(Integer questionId) {
		return dao.selectOne(questionId);
	}
	
	public List<Question> selectMany(){
		return dao.selectMany();
	}

	public boolean updateOne(Question question) {
		int rowNumber = dao.updateOne(question);
		boolean result = false;
		if(rowNumber >0) {
			result = true;
			return result;
		}
		return result;
	}
	public boolean deleteOne(Integer questionId) {
		int rowNumber = dao.deleteOne(questionId);
		boolean result = false;
		if(rowNumber > 0){
			result = true;
		}
		return result;
	}
	public void questionCsvOut() throws DataAccessException{
		dao.questionCsvOut();
	}
	public byte[] getFile(String fileName) throws IOException{
		FileSystem fs = FileSystems.getDefault();
		Path p = fs.getPath(fileName);
		byte[] bytes = Files.readAllBytes(p);
		return bytes;
	}
	
	
	public List<String> groupByCategory(){
		return dao.groupByCategory();
	}
	
	public List<Question> getSelectCategoryList(List<String> selectedCategories){
		return dao.selectCategoryList(selectedCategories);
	}
	
	public List<Question> getRandomQuestions(List<Question> questionList,int count){
		List<Question> shuffledList = shuffleList(questionList);
		return extractQuestions(shuffledList, count);
	}
	

    
    
    
    
}

