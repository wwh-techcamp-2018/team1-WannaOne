package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.service.NoteService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiNoteAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(ApiNoteAcceptanceTest.class);
    @Autowired
    private NoteService noteService;

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
                getForEntityWithParameterized("/api/notes/all", null, new ParameterizedTypeReference<List<Note>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(0).getTitle()).isEqualTo("첫번째 제목");
        assertThat(response.getBody().get(0).getText()).isEqualTo("첫번째 내용");
        //TODO response body 보다 엄밀하게 확인하기
    }


    @Test
    public void post() {
        Note postNote = new Note("내가 쓴 첫번 째 노트", "우아노트는 21세기 현대인을 위한 최고의 노트입니다.");
        ResponseEntity<Note> response = template().postForEntity("/api/notes/1", postNote, Note.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getTitle()).isEqualTo("내가 쓴 첫번 째 노트");
        assertThat(response.getBody().getId()).isNotNull();
        log.info("note info, {}", response.getBody());
    }

    //TODO : update Note

    //TODO : remove Note
}