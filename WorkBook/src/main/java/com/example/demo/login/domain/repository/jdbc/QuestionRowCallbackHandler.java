package com.example.demo.login.domain.repository.jdbc;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowCallbackHandler;

public class QuestionRowCallbackHandler implements RowCallbackHandler{
	@Override
	public void processRow(ResultSet rs)throws SQLException{
		try {
			File file = new File("question_list.csv");
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			do {
				String str = rs.getString("question_id")+","
						+rs.getString("category")+","
						+rs.getString("question_statement")+","
						+rs.getString("choice1")+","
						+rs.getString("choice2")+","
						+rs.getString("choice3")+","
						+rs.getString("choice4")+","
						+rs.getString("answer")+","
						+rs.getString("explanation")+","
						+rs.getString("answered")+","
						+rs.getString("result");
				bw.write(str);
				bw.newLine();
				
			}while(rs.next());
			bw.flush();
			bw.close();
			
		}catch(IOException e) {
			e.printStackTrace();
			throw new SQLException(e);
		}
	}
}