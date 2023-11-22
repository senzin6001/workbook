package com.example.demo.login.domain.service;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
	
    /**
     * データベースにQuestionを挿入します。
     *
     * @param question 挿入するQuestionオブジェクト
     * @return 挿入成功の場合はtrue、それ以外はfalse
     */
    public boolean insert(Question question) {
        int rowNumber = dao.insertOne(question);
        return rowNumber > 0;
    }
	
    /**
     * Questionの総数を取得します。
     *
     * @return Questionの総数
     */
    public int count() {
        return dao.count();
    }
    /**
     * 指定されたIDのQuestionを取得します。
     *
     * @param questionId 取得するQuestionのID
     * @return 取得したQuestionオブジェクト
     */
    public Question selectOne(Integer questionId) {
        return dao.selectOne(questionId);
    }
    /**
     * Questionのリストを取得します。
     *
     * @return Questionのリスト
     */
    public List<Question> selectMany() {
        return dao.selectMany();
    }
    /**
     * Questionを更新します。
     *
     * @param question 更新するQuestionオブジェクト
     * @return 更新成功の場合はtrue、それ以外はfalse
     */
    public boolean updateOne(Question question) {
        int rowNumber = dao.updateOne(question);
        return rowNumber > 0;
    }
    /**
     * 指定されたIDのQuestionを削除します。
     *
     * @param questionId 削除するQuestionのID
     * @return 削除成功の場合はtrue、それ以外はfalse
     */
    public boolean deleteOne(Integer questionId) {
        int rowNumber = dao.deleteOne(questionId);
        return rowNumber > 0;
    }
	 /**
     * QuestionのCSVファイルを出力します。
     *
     * @throws DataAccessException データアクセスエラーが発生した場合
     */
    public void questionCsvOut() throws DataAccessException {
        dao.questionCsvOut();
    }	
    /**
     * 指定されたファイル名のバイトデータを取得します。
     *
     * @param fileName 取得するファイルの名前
     * @return ファイルのバイトデータ
     * @throws IOException 入出力エラーが発生した場合
     */
    public byte[] getFile(String fileName) throws IOException {
        FileSystem fs = FileSystems.getDefault();
        Path path = fs.getPath(fileName);
        return Files.readAllBytes(path);
    }
    /**
     * Questionのカテゴリごとのグループ化を取得します。
     *
     * @return カテゴリごとのQuestionリスト
     */
    public List<String> groupByCategory() {
        return dao.groupByCategory();
    }
    /**
     * 選択されたカテゴリに基づいてQuestionリストをフィルタリングします。
     *
     * @param questionList       フィルタリング対象のQuestionリスト
     * @param selectedCategories 選択されたカテゴリのリスト
     * @return フィルタリングされたQuestionリスト
     */
    public List<Question> filterQuestionsByCategoryList(List<Question> questionList, List<String> selectedCategories) {
        return questionList.stream()
                .filter(question -> selectedCategories.contains(question.getCategory()))
                .collect(Collectors.toList());
    }
    /**
     * 不正解のQuestionをフィルタリングします。
     *
     * @param questionList フィルタリング対象のQuestionリスト
     * @return フィルタリングされたQuestionリスト
     */    
    public List<Question> filterIncorrectQuestions(List<Question> questionList) {
        return questionList.stream()
                .filter(question -> !question.isResult())
                .collect(Collectors.toList());
    }
    /**
     * ランダムにシャッフルされたQuestionリストを取得します。
     *
     * @param questionList シャッフルするQuestionリスト
     * @return シャッフルされたQuestionリスト
     */	
    public List<Question> getRandomQuestions(List<Question> questionList) {
        List<Question> shuffledList = new ArrayList<>(questionList);
        Collections.shuffle(shuffledList);
        return shuffledList;
    }
    /**
     * Questionテーブルを初期化します。
     */
    public void initQuestionTable() {
        dao.initQuestionTable();
    }
    /**
     * Questionリストをデータベースに挿入します。
     *
     * @param questionList 挿入するQuestionリスト
     */
    public void insertQuestionList(List<Question> questionList) {
        dao.insertQuestionList(questionList);
    }
    /**
     * QuestionリストからQuestionのIDリストを取得します。
     *
     * @param questions 取得するQuestionリスト
     * @return QuestionのIDリスト
     */
    public List<Integer> getQuestionIds(List<Question> questions) {
        return questions.stream()
                .map(Question::getQuestionId)
                .collect(Collectors.toList());
    }
    /**
     * 指定された数だけQuestionリストを抽出します。
     *
     * @param questionList 対象のQuestionリスト
     * @param count        抽出するQuestionの数
     * @return 抽出されたQuestionリスト
     */	
    public List<Question> extractQuestions(List<Question> questionList, int count) {
        if (questionList == null || count <= 0) {
            return Collections.emptyList();
        }

        return questionList.stream()
                .limit(count)
                .collect(Collectors.toList());
    }
    /**
     * 未回答のQuestionをフィルタリングします。
     *
     * @return フィルタリングされたQuestionリスト
     */ 
    public List<Question> filterUnansweredQuestions() {
        return dao.filterUnansweredQuestions();
    }
    /**
     * ログイン中のユーザーに関連するQuestionの総数を取得します。
     *
     * @return ログイン中のユーザーに関連するQuestionの総数
     */    
    public int countMyQuestion() {
        return dao.myQuestionCount();
    }
    /**
     * Questionの回答結果を保存します。
     *
     * @param questionId QuestionのID
     * @param result     回答結果（正解かどうか）
     */
    public void saveResult(int questionId, boolean result) {
        dao.saveResult(questionId, result);
    }
    /**
     * Questionが回答済みであることを保存します。
     *
     * @param questionId QuestionのID
     */
    public void saveAnswered(int questionId) {
    	dao.saveAnswered(questionId);
    }
    /**
     * 正解したQuestionの総数を取得します。
     *
     * @return 正解したQuestionの総数
     */
    public int countCorrectAnswer() {
    	return dao.countCorrectAnswer();
    }
}

