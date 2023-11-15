INSERT INTO m_user (email, password, user_name, birthday, age,marriage, role)
VALUES('yamada@xxx.co.jp','{bcrypt}$2a$10$jr3A85H3Pm.RrMEK7nagx.G/OE3hwOnM75vLvAIGobTtaI9nDlWzm','山田太郎','1990-01-01',28,false,'ROLE_ADMIN');

INSERT INTO m_user (email, password, user_name, birthday, age, marriage, role)
VALUES('tamura@xxx.co.jp','{bcrypt}$2a$10$jr3A85H3Pm.RrMEK7nagx.G/OE3hwOnM75vLvAIGobTtaI9nDlWzm','田村達也','1986-11-05',31,false,'ROLE_GENERAL');


INSERT INTO m_question (category, question_statement, choice1, choice2, choice3, choice4, answer, explanation, answered ,result)
VALUES('バリデーション','問題文(仮)','選択肢１','選択肢２','選択肢３','選択肢４',1,'解説',false,false);