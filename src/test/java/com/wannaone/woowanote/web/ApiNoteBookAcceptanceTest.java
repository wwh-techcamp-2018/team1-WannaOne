package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.exception.ErrorDetails;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiNoteBookAcceptanceTest extends AcceptanceTest {
    @Autowired
    private MessageSourceAccessor msa;

    @Test
    public void showAllNoteBooks() {
        ResponseEntity<List<NoteBook>> response =
                getForEntityWithParameterizedWithBasicAuth("/api/notebooks", null, new ParameterizedTypeReference<List<NoteBook>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(0).getTitle()).isEqualTo("AWS 공부");
    }

    @Test
    public void showAllNoteBooks_when_no_login() {
        ResponseEntity<ErrorDetails> response = getForEntity("/api/notebooks", null, ErrorDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).isEqualTo(msa.getMessage("unauthentication.not.logined"));
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
