CREATE TABLE IF NOT EXISTS m_user (
	user_id INT PRIMARY KEY AUTO_INCREMENT, -- AUTO_INCREMENT を追加
	email VARCHAR(256),
	password VARCHAR(100),
	user_name VARCHAR(50),
	birthday DATE,
	age INT,
	role VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS m_question (
	question_id INT PRIMARY KEY AUTO_INCREMENT, -- AUTO_INCREMENT を追加
	category VARCHAR(30),
	question_statement VARCHAR(256),
	choice1 VARCHAR(50),
	choice2 VARCHAR(50),
	choice3 VARCHAR(50),
	choice4 VARCHAR(50),
	answer INT,
	explanation VARCHAR(256),
	answered BOOLEAN,
	result BOOLEAN
);

CREATE TABLE IF NOT EXISTS my_question (
	question_id INT,
	category VARCHAR(30),
	question_statement VARCHAR(256),
	choice1 VARCHAR(50),
	choice2 VARCHAR(50),
	choice3 VARCHAR(50),
	choice4 VARCHAR(50),
	answer INT,
	explanation VARCHAR(256),
	answered BOOLEAN,
	result BOOLEAN
);