-- insert into board(no, title, writer, content, password, write_time, readcount, reply_count) 
-- values(1, '테스트게시글1', 'testuser', '테스트내용1', 1, now(), 0, 0);

-- 저장 프로시저 1

DELIMITER //

CREATE PROCEDURE InsertBoardRecords()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= 2823 DO
        INSERT INTO board(no, title, writer, content, password, write_time, readcount, reply_count)
        VALUES(i, CONCAT('테스트게시글', i), 'testuser', CONCAT('테스트내용', i), 'testpassword', NOW(), 0, 0);

        SET i = i + 1;
    END WHILE;
END //

DELIMITER ;

-- 저장 프로시저 먼저 생성 후 프로시저 호출하기

-- 저장 프로시저를 호출하여 2823개의 레코드를 삽입합니다.
CALL InsertBoardRecords();

-- STS 에선 저장 프로시저 자동 실행 X 직접 DB에 이 쿼리문 실행하기

--------------------------------------------------------------
-- 아래는 'testpassword'를 1, 2, 3,... 순차적으로 사용하는 방식으로 수정 
-- 저장프로시저 2

DELIMITER //

CREATE PROCEDURE InsertBoardRecords()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= 2823 DO
        INSERT INTO board(no, title, writer, content, password, write_time, readcount, reply_count)
        VALUES(i, CONCAT('테스트게시글', i), 'testuser', CONCAT('테스트내용', i), i, NOW(), 0, 0);

        SET i = i + 1;
    END WHILE;
END //

DELIMITER ;

-- 저장 프로시저를 호출하여 2823개의 레코드를 삽입합니다.
CALL InsertBoardRecords();
-- 여기서 password 필드에 i를 직접 할당하여 순차적인 값을 제공하도록 변경하였습니다.

-- insert into BOARD(NO, WRITER, TITLE, CONTENT, READCOUNT, WRITE_TIME) VALUES(1, 'testuser', '테스트1', '테스트1', 0, now());
-- insert into BOARD(NO, WRITER, TITLE, CONTENT, READCOUNT, WRITE_TIME) VALUES(2, 'testuser', '테스트2', '테스트2', 0, now());
-- insert into BOARD(NO, WRITER, TITLE, CONTENT, READCOUNT, WRITE_TIME) VALUES(3, 'testuser', '테스트3', '테스트3', 0, now());
-- insert into BOARD(NO, WRITER, TITLE, CONTENT, READCOUNT, WRITE_TIME) VALUES(4, 'testuser', '테스트4', '테스트4', 0, now());
-- insert into BOARD(NO, WRITER, TITLE, CONTENT, READCOUNT, WRITE_TIME) VALUES(5, 'testuser', '테스트5', '테스트5', 0, now());
-- insert into BOARD(NO, WRITER, TITLE, CONTENT, READCOUNT, WRITE_TIME) VALUES(6, 'testuser', '테스트6', '테스트6', 0, now());
-- insert into BOARD(NO, WRITER, TITLE, CONTENT, READCOUNT, WRITE_TIME) VALUES(7, 'testuser', '테스트7', '테스트7', 0, now());
-- insert into BOARD(NO, WRITER, TITLE, CONTENT, READCOUNT, WRITE_TIME) VALUES(8, 'testuser', '테스트8', '테스트8', 0, now());
-- insert into BOARD(NO, WRITER, TITLE, CONTENT, READCOUNT, WRITE_TIME) VALUES(9, 'testuser', '테스트9', '테스트9', 0, now());
-- insert into BOARD(NO, WRITER, TITLE, CONTENT, READCOUNT, WRITE_TIME) VALUES(10, 'testuser', '테스트10', '테스트10', 0, now());


-- insert into BOARD(NO, WRITER, TITLE, CONTENT, PASSWORD, READCOUNT, WRITE_TIME, reply_count) VALUES(1, 'testuser', '테스트1', '테스트1', 1 ,0, now(), 0);
-- insert into BOARD(NO, WRITER, TITLE, CONTENT, PASSWORD, READCOUNT, WRITE_TIME, reply_count) VALUES(2, 'testuser', '테스트2', '테스트2', 2, 0, now(), 0);
-- insert into BOARD(NO, WRITER, TITLE, CONTENT, PASSWORD, READCOUNT, WRITE_TIME, reply_count) VALUES(3, 'testuser', '테스트3', '테스트3', 3, 0, now(), 0);
