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
    public void showAllNotes() {
        ResponseEntity<List<NoteBook>> response =
                getForEntityWithParameterized("/api/notebooks", null, new ParameterizedTypeReference<List<NoteBook>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(0).getTitle()).isEqualTo("AWS 공부");
    }
}
