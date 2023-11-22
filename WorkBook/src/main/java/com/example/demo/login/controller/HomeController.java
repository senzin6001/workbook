package com.example.demo.login.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.login.domain.model.GroupOrder;
import com.example.demo.login.domain.model.Question;
import com.example.demo.login.domain.model.QuestionCreationForm;
import com.example.demo.login.domain.model.SignupForm;
import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.service.QuestionService;
import com.example.demo.login.domain.service.UserService;

@Controller
public class HomeController{
		
	private final QuestionService questionService;
	private final UserService userService;
	
	private Map<String,String> radioMarriage;
	private Map<String,String> radioAnswered;
	private Map<String,String> radioResult;
	private Map<String,String> radioIncorrect;
		
    public HomeController(QuestionService questionService, UserService userService) {
    	  	
        this.questionService = questionService;
        this.userService = userService;
        
    }
	
	private Map<String,String> initRadioMarriage(){
				
		Map<String,String> radio =new LinkedHashMap<>();
		
		radio.put("既婚", "true");
		radio.put("未婚", "false");
		
		return radio;
		
	}
	
    private void initializeMarriageRadioMaps(Model model) {
        radioMarriage = initRadioMarriage();
        model.addAttribute("radioMarriage", radioMarriage);
    }
		
	private Map<String,String> initRadioAnswered(){
				
		Map<String,String> radio = new LinkedHashMap<>();
		
		radio.put("解答済み", "true");
		radio.put("未回答", "false");
		
		return radio;
		
	}
	
    private void initializeAnsweredRadioMaps(Model model) {
        radioAnswered = initRadioAnswered();
        model.addAttribute("radioAnswered", radioAnswered);
    }
		
	private Map<String,String> initRadioResult(){
		
		Map<String,String> radio = new LinkedHashMap<>();
		
		radio.put("正解", "true");
		radio.put("不正解", "false");
		
		return radio;	
		
	}
	
    private void initializeResultRadioMaps(Model model) {
        radioResult = initRadioResult();
        model.addAttribute("radioResult", radioResult);
    }
		
	private Map<String,String> initRadioIncorrect(){
				
		Map<String,String> radio = new LinkedHashMap<>();
		
		radio.put("すべて", "true");
		radio.put("不正解のみ", "false");
		
		return radio;
		
	}
		
    private void initializeIncorrectRadioMaps(Model model) {
        radioIncorrect = initRadioIncorrect();
        model.addAttribute("radioIncorrect", radioIncorrect);
    }
    
    
    

		
	private Question convertQuestionCreationFormToQuestion(QuestionCreationForm form) {
				
		Question question = new Question();
		question.setQuestionId(form.getQuestionId());
		question.setCategory(form.getCategory());
		question.setQuestionStatement(form.getQuestionStatement());
		question.setChoice1(form.getChoice1());
		question.setChoice2(form.getChoice2());
		question.setChoice3(form.getChoice3());
		question.setChoice4(form.getChoice4());
		question.setAnswer(form.getAnswer());
		question.setExplanation(form.getExplanation());
		question.setAnswered(form.isAnswered());
		question.setResult(form.isResult());
		
	    return question;  
	    
	}
	
	private User convertSignupFormToUser(SignupForm form) {
		
		User user = new User();
		user.setUserId(form.getUserId());
		user.setEmail(form.getEmail());
		user.setPassword(form.getPassword());
		user.setUserName(form.getUserName());
		user.setBirthday(form.getBirthday());
		user.setAge(form.getAge());
		user.setMarriage(form.isMarriage());
		
	    return user;  
	    
	}
	

	@GetMapping("/home")
	public String getHome(Model model ) {		
		initializeIncorrectRadioMaps(model);
		
		List<String>categoryList = questionService.groupByCategory();
		model.addAttribute("categoryList",categoryList);

		int count = questionService.count();
		model.addAttribute("questionAllCount",count);
		
		model.addAttribute("contents","login/home :: home_contents");
		
		return "login/homeLayout";
		
	}
	
	@GetMapping("/start")
	public String initQuestion(
	        @RequestParam(name = "categories", required = false) List<String> selectedCategories,
	        @RequestParam int questionCount,
	        @RequestParam(name = "selection") String selection,
	        Model model) {
		
		//出題テーブルを初期化
	    questionService.initQuestionTable();
	    List<Question> questionList = new ArrayList<>();
	    questionList = questionService.selectMany();
	    if (selectedCategories != null && !selectedCategories.isEmpty()) {
	        questionList = questionService.filterQuestionsByCategoryList(questionList,selectedCategories);
	    }
	    
		System.out.println("selection"+selection);
	    if("incorrect".equals(selection)) {
			System.out.println("通った");
	    	questionList = questionService.filterIncorrectQuestions(questionList);
	    }
	    questionList = questionService.getRandomQuestions(questionList);   
	    if(questionList.size() > questionCount) {
	    	questionList = questionService.extractQuestions(questionList, questionCount);
	    }
		
		if(questionList.size() < 1) {
			model.addAttribute("message","問題数が0です。");
			model.addAttribute("contents","login/home :: home_contents");
			return getHome(model);
		}
		
	    //出題テーブルに登録
	    questionService.insertQuestionList(questionList);
	    
	    return getQuestion(model);
	}
	@GetMapping("/login/question")
	public String getQuestion(Model model) {
		//出題テーブルから解答済以外のidを取得
		List<Question> questionList = new ArrayList<>();
		questionList = questionService.filterUnansweredQuestions();
		
		//全問が終了
		if (questionList.size() == 0) {
			return getResult(model);
		}
		//出題数を取得
		int countMyQuestion = questionService.countMyQuestion();
		model.addAttribute("totalQuestionNumber","全" + countMyQuestion + "問中");
		//現在何問目か
		int nowQuestionNumber = countMyQuestion - questionList.size() + 1;
		model.addAttribute("nowQuestionNumber",nowQuestionNumber + "問目");
		//出題
	    model.addAttribute("question",questionService.selectOne(questionList.get(0).getQuestionId()));
	    model.addAttribute("contents","login/question :: question_contents");
		return "login/homeLayout";
		
	}
	
	@GetMapping("/login/result")
	public String getResult(Model model) {
		
		int countMyQuestion = questionService.countMyQuestion();
		model.addAttribute("countMyQuestion", countMyQuestion);		
		int countCorrectAnswer = questionService.countCorrectAnswer();
		model.addAttribute("countCorrectAnswer", countCorrectAnswer);

		double correctAnswerRate = (double)countCorrectAnswer/countMyQuestion * 100;
		model.addAttribute("correctAnswerRate", correctAnswerRate);
	    model.addAttribute("contents","login/result :: result_contents");
		return "login/homeLayout";
		
	}
	
	@PostMapping("/login/answer")
	public String answer(@RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "answer", required = false) int answer,
			Model model) {
		
		if (answer == questionService.selectOne(id).getAnswer()) {
			questionService.saveResult(id,true);
		}else {
			questionService.saveResult(id,false);
				
		}
		questionService.saveAnswered(id);	
		return getQuestion(model);
		
	}

	@GetMapping("/userList")
	public String getUserList(Model model) {
		model.addAttribute("contents","login/userList :: userList_contents");
		
		List<User> userList = userService.selectMany();
		
		model.addAttribute("userList",userList);
		
		int count = userService.count();
		model.addAttribute("userListCount",count);
		return "login/homeLayout";
	}
	
	
	@GetMapping("/userDetail/{id:.+}")
	public String getUserDetail(@ModelAttribute SignupForm form, Model model,
			@PathVariable("id") Integer userId) {
		System.out.println("userId="+ userId);
		// ::は<div th:fragment="userDetail_contents">
		model.addAttribute("contents","login/userDetail::userDetail_contents");
		
		initializeMarriageRadioMaps(model);
		
		if(userId !=null && userId > 0) {
			User user = userService.selectOne(userId);
			form.setUserId(user.getUserId());
			form.setEmail(user.getEmail());
			form.setUserName(user.getUserName());
			form.setBirthday(user.getBirthday());
			form.setAge(user.getAge());
			form.setMarriage(user.isMarriage());
			model.addAttribute("signupForm",form);
		}
		return "login/homeLayout";
	}
	
	@PostMapping(value ="/userDetail",params="update")
	public String postUserDetailUpdate(@ModelAttribute @Validated(GroupOrder.class) SignupForm form,
			BindingResult bindingResult,
			Model model) {
		System.out.println("更新ボタンの処理");
		
		if(bindingResult.hasErrors()) {	
			return getUserDetail(form,model,form.getUserId());
		}
		
		User user = convertSignupFormToUser(form);
		
		try {
		
			boolean result = userService.updateOne(user);
			if(result == true) {
				model.addAttribute("result","更新成功");
			}else {
				model.addAttribute("result","更新失敗");
			}
		}catch(DataAccessException e) {
			model.addAttribute("result","更新失敗(トランザクションテスト)");
		}
		return getUserList(model);
	}
	
	@PostMapping(value="/userDetail",params="delete")
	public String postUserDetailDelete(@ModelAttribute SignupForm form,Model model) {
		System.out.println("削除ボタンの処理");
		boolean result = userService.deleteOne(form.getUserId());
		if(result == true) {
			model.addAttribute("result","削除成功");
		}else {
			model.addAttribute("result","削除失敗");
		}
		return getUserList(model);
	}
		
	@GetMapping("/questionCreation")
	public String getQuestionCreationForm(@ModelAttribute QuestionCreationForm form,Model model) {
		
		model.addAttribute("contents","login/questionCreation :: question_creation_contents");
		initializeAnsweredRadioMaps(model);
		initializeResultRadioMaps(model);
		
		List<String>categoryList = questionService.groupByCategory();
				
		model.addAttribute("categoryList",categoryList);
		
		return "login/homeLayout";
		
	}
	
	
	@PostMapping(value="/questionCreation",params="creation")
	public String postQuestionCreation(@ModelAttribute @Validated(GroupOrder.class) QuestionCreationForm form,
			BindingResult bindingResult,
			Model model) {
		
			if(bindingResult.hasErrors()) {	
				return getQuestionCreationForm(form,model);
			}

			System.out.println(form);
			
			Question question = convertQuestionCreationFormToQuestion(form);
			

			boolean result = questionService.insert(question);
			
			if(result == true) {
				System.out.println("insert成功");
			} else {
				System.out.println("insert失敗");
			}
			
			return "redirect:/questionCreation";
	}

	@GetMapping("/questionList")
	public String getQuestionList(Model model) {
		model.addAttribute("contents","login/questionList :: questionList_contents");
		
		List<Question> questionList = questionService.selectMany();
		
		model.addAttribute("questionList",questionList);
		
		int count = questionService.count();
		model.addAttribute("questionListCount",count);
		return "login/homeLayout";
	}
	@GetMapping("/questionManagement")	
	public String getQuestionManagement(Model model) {
		model.addAttribute("contents","login/questionManagement :: questionManagement_contents");
		
		List<Question> questionManagement = questionService.selectMany();
		
		model.addAttribute("questionManagement",questionManagement);
		
		int count = questionService.count();
		model.addAttribute("questionManagementCount",count);
		return "login/homeLayout";
	}
	
	@GetMapping("/questionDetail/{id:.+}")
	public String getQuestionDetail(@ModelAttribute QuestionCreationForm form, Model model,
			@PathVariable("id") Integer questionId) {
		System.out.println("questionId="+ questionId);
		// ::は<div th:fragment="questionDetail_contents">

		initializeAnsweredRadioMaps(model);
		initializeResultRadioMaps(model);
		
		List<String>categoryList = questionService.groupByCategory();
		Collections.sort(categoryList);
		model.addAttribute("categoryList",categoryList);
		model.addAttribute("contents","login/questionDetail::questionDetail_contents");

		if(questionId !=null && questionId > 0) {
			Question question = questionService.selectOne(questionId);
			form.setQuestionId(question.getQuestionId());
			form.setCategory(question.getCategory());
			form.setQuestionStatement(question.getQuestionStatement());
			form.setChoice1(question.getChoice1());
			form.setChoice2(question.getChoice2());
			form.setChoice3(question.getChoice3());
			form.setChoice4(question.getChoice4());
			form.setAnswer(question.getAnswer());
			form.setExplanation(question.getExplanation());
			form.setAnswered(question.isAnswered());
			form.setResult(question.isResult());
			
			model.addAttribute("questionCreationForm",form);
		}
		return "login/homeLayout";
	}
	
	@PostMapping(value ="/questionDetail",params="update")
	public String postQuestionDetailUpdate(@ModelAttribute @Validated(GroupOrder.class) QuestionCreationForm form,
			BindingResult bindingResult,
			Model model) {
		System.out.println("更新ボタンの処理");
		
		if(bindingResult.hasErrors()) {	
			return getQuestionDetail(form,model,form.getQuestionId());
		}
		
		Question question = convertQuestionCreationFormToQuestion(form);
		
		try {
		
			boolean result = questionService.updateOne(question);
			if(result == true) {
				model.addAttribute("result","更新成功");
			}else {
				model.addAttribute("result","更新失敗");
			}
		}catch(DataAccessException e) {
			model.addAttribute("result","更新失敗(トランザクションテスト)");
		}
		return getQuestionManagement(model);
	}
	
	@PostMapping(value="/questionDetail",params="delete")
	public String postQuestionDetailDelete(@ModelAttribute QuestionCreationForm form,Model model) {
		System.out.println("削除ボタンの処理");
		boolean result = questionService.deleteOne(form.getQuestionId());
		if(result == true) {
			model.addAttribute("result","削除成功");
		}else {
			model.addAttribute("result","削除失敗");
		}
		return getQuestionManagement(model);
	}
	
	@PostMapping("/logout")
	public String postLogout() {
		return "redirect:/login";
	}
	
	@GetMapping("/userList/csv")
	public ResponseEntity<byte[]> getUserListCsv(Model model) {
		
		userService.userCsvOut();
		byte[] bytes =null;
		try {
			bytes = userService.getFile("user_list.csv");
		}catch(IOException e) {
			e.printStackTrace();
		}
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "text/csv; charset=UTF-8");
		header.setContentDispositionFormData("filename", "user_list.csv");
		return new ResponseEntity<>(bytes,header,HttpStatus.OK);
	}
	
	@GetMapping("/questionList/csv")
	public ResponseEntity<byte[]> getQuestionListCsv(Model model) {
		
		questionService.questionCsvOut();
		byte[] bytes =null;
		try {
			bytes = questionService.getFile("question_list.csv");
		}catch(IOException e) {
			e.printStackTrace();
		}
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "text/csv; charset=UTF-8");
		header.setContentDispositionFormData("filename", "question_list.csv");
		return new ResponseEntity<>(bytes,header,HttpStatus.OK);
	}
	@GetMapping("/questionManagement/csv")
	public ResponseEntity<byte[]> getQuestionManagementCsv(Model model) {
		
		questionService.questionCsvOut();
		byte[] bytes =null;
		try {
			bytes = questionService.getFile("question_list.csv");
		}catch(IOException e) {
			e.printStackTrace();
		}
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "text/csv; charset=UTF-8");
		header.setContentDispositionFormData("filename", "question_list.csv");
		return new ResponseEntity<>(bytes,header,HttpStatus.OK);
	}
	@GetMapping("/admin")
	public String getAdmin(Model model) {
		model.addAttribute("contents","login/admin::admin_contents");
		return "login/homeLayout";
	}
	
}
