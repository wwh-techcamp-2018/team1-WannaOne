package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.service.NoteService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiNoteAcceptanceTest extends AcceptanceTest {
    @Autowired
    private NoteService noteService;

    @Test
    public void show() {
        ResponseEntity<Note> response =  template().getForEntity("/api/notes/1", Note.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("첫번째 제목");
        assertThat(response.getBody().getText()).isEqualTo("첫번째 내용");
    }

    //TODO 요청한 노트가 없을 때 에러 메시지 처리


    @Test
    public void post() {
        //note 의 id 를 받아오도록
        Note postNote = new Note("내가 쓴 첫번 째 노트", "우아노트는 21세기 현대인을 위한 최고의 노트입니다.");
        ResponseEntity<Long> response = template().postForEntity("/api/notes", postNote, Long.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(noteService.getNote(response.getBody()).getTitle()).isEqualTo("내가 쓴 첫번 째 노트");
    }

    //TODO : update Note

    //TODO : remove Note
}