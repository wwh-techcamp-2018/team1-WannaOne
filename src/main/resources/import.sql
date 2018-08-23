INSERT INTO user(id, email, password, name) VALUES (1, 'doy@woowahan.com', '$2a$10$b/fsUrWITZQ1CpB.VhaVvOmUo8fqbKoZPy4f/RwarSaBfN0.0Jk7e', 'doy');
INSERT INTO user(id, email, password, name) VALUES (2, 'dain@woowahan.com', '$2a$10$b/fsUrWITZQ1CpB.VhaVvOmUo8fqbKoZPy4f/RwarSaBfN0.0Jk7e', 'dain');
INSERT INTO note_book(id, title, owner_id) values (1, 'AWS 공부', 1);
INSERT INTO note_book(id, title, owner_id) values (2, '단위 테스트, TDD', 1);
INSERT INTO note_book(id, title, owner_id) values (3, '자바 웹 프로그래밍', 1);
INSERT INTO note_book(id, title, owner_id) values (4, 'ATDD 기반 웹 프로그래밍', 1);
INSERT INTO note_book(id, title, owner_id) values (5, 'HTML,CSS, JS 기초', 2);
INSERT INTO note_book(id, title, owner_id) values (6, '배민찬 서비스 개발', 2);
INSERT INTO note_book(id, title, owner_id) values (7, '우아한 프로젝트', 2);
INSERT INTO note(id, title, text, register_datetime, update_datetime, note_book_id, deleted, writer_id) values (1, '첫번째 제목', '첫번째 내용', '2018-08-14 13:00:00', '2018-08-14 13:54:00', 1, false, 1);
INSERT INTO note(id, title, text, register_datetime, update_datetime, note_book_id, deleted, writer_id) values (2, '두번째 제목', '두번째 내용', '2018-08-14 13:00:00', '2018-08-14 13:54:00', 1, false, 1);
INSERT INTO note(id, title, text, register_datetime, update_datetime, note_book_id, deleted, writer_id) values (3, '세번째 제목', '세번째 내용', '2018-08-14 13:00:00', '2018-08-14 13:54:00', 1, false, 1);
INSERT INTO note(id, title, text, register_datetime, update_datetime, note_book_id, deleted, writer_id) values (4, '네번째 제목', '네번째 내용', '2018-08-14 13:00:00', '2018-08-14 13:54:00', 2, false, 1);
INSERT INTO comment(id, content, register_datetime, update_datetime, note_id, writer_id) values(1, '첫 댓글', '2018-08-14 13:00:00', '2018-08-14 13:54:00', 1, 2);
INSERT INTO comment(id, content, register_datetime, update_datetime, note_id, writer_id) values(2, '아이디어가 떨어지네요...', '2018-08-14 13:00:00', '2018-08-14 13:54:00', 1, 1);
INSERT INTO comment(id, content, register_datetime, update_datetime, note_id, writer_id) values(3, '우아노트 화이팅!!!', '2018-08-14 13:00:00', '2018-08-14 13:54:00', 1, 2);
INSERT INTO comment(id, content, register_datetime, update_datetime, note_id, writer_id) values(4, '두번째 글의 첫 댓글', '2018-08-14 13:00:00', '2018-08-14 13:54:00', 2, 1);

