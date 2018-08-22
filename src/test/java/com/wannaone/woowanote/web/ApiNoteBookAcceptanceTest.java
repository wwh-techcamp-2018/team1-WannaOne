package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.NoteBook;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiNoteBookAcceptanceTest extends AcceptanceTest {
    @Test
    public void showAllNoteBooks() {
        // TODO: basicAuth 넣기
        ResponseEntity<List<NoteBook>> response =
                getForEntityWithParameterized("/api/notebooks", null, new ParameterizedTypeReference<List<NoteBook>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(0).getTitle()).isEqualTo("AWS 공부");
    }

    @Test
    public void createNoteBookTest() {
        String noteBookName = "내가 쓴 첫번 째 노트북";
        NoteBook noteBook = new NoteBook(noteBookName);
        ResponseEntity<NoteBook> response = template().postForEntity("/api/notebooks", noteBook, NoteBook.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getTitle()).isEqualTo(noteBookName);
        assertThat(response.getBody().getId()).isNotNull();
    }
}
