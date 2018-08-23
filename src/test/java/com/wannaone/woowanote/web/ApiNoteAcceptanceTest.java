package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiNoteAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(ApiNoteAcceptanceTest.class);

    @Test
    public void show() {
        ResponseEntity<Note> response = template().getForEntity("/api/notes/1", Note.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("첫번째 제목");
        assertThat(response.getBody().getText()).isEqualTo("첫번째 내용");
    }

    //TODO 요청한 노트가 없을 때 에러 메시지 처리

    @Test
    public void showAllNotes() {
        ResponseEntity<List<Note>> response =
                getForEntityWithParameterized("/api/notes", null, new ParameterizedTypeReference<List<Note>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(0).getTitle()).isEqualTo("첫번째 제목");
        assertThat(response.getBody().get(0).getText()).isEqualTo("첫번째 내용");
        //TODO response body 보다 엄밀하게 확인하기
    }


    @Test
    public void create_with_loginUser() {
        User loginUser = defaultUser();
        ResponseEntity<Note> response = basicAuthTemplate(loginUser).postForEntity("/api/notes/notebook/1", null, Note.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("제목 없음");
        assertThat(response.getBody().getWriter().getEmail()).isEqualTo(loginUser.getEmail());
        assertThat(response.getBody().getText()).isEmpty();
        log.info("note info, {}", response.getBody());
    }

    @Test
    public void update() {
        Note updateNote = new Note("내가 수정한 두 번째 노트", "우아노트는 최고의 노트입니다.");
        ResponseEntity<Note> response = putForEntity("/api/notes/2", updateNote, Note.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo(updateNote.getTitle());
        assertThat(response.getBody().getText()).isEqualTo(updateNote.getText());
    }

    @Test
    public void delete() {
        ResponseEntity<Note> postResponse = basicAuthTemplate().postForEntity("/api/notes/notebook/1", null, Note.class);
        Long noteId = postResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = deleteForEntity("/api/notes/" + noteId, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Void> errorResponse = basicAuthTemplate().getForEntity("/api/notes/" + noteId, Void.class);
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //TODO 다른 사람의 노트 삭제시 에러
}