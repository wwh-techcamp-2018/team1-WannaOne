package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.dto.NoteBookDto;
import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.exception.ErrorDetails;
import com.wannaone.woowanote.support.ErrorMessage;
import com.wannaone.woowanote.validation.ValidationErrorsResponse;
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
        assertThat(response.getBody().get(0).getTitle()).isEqualTo("tech");
    }

    @Test
    public void showAllNoteBooks_when_no_login() {
        ResponseEntity<ErrorDetails> response = getForEntity("/api/notebooks", null, ErrorDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getMessage()).isEqualTo(ErrorMessage.REQUIRE_LOGIN.getMessageKey());
    }

    @Test
    public void createNoteBookTest() {
        String noteBookName = "내가 쓴 첫번 째 노트북";
        NoteBookDto noteBookDto = new NoteBookDto(noteBookName);
        ResponseEntity<NoteBook> response = basicAuthTemplate().postForEntity("/api/notebooks", noteBookDto, NoteBook.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getTitle()).isEqualTo(noteBookName);
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    public void getNoteBookByNoteBookId() {
        String noteBookName = "내가 쓴 첫번 째 노트북";
        NoteBookDto noteBookDto = new NoteBookDto(noteBookName);
        ResponseEntity<NoteBook> response = basicAuthTemplate(defaultUser()).postForEntity("/api/notebooks", noteBookDto, NoteBook.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getTitle()).isEqualTo(noteBookName);
        assertThat(response.getBody().getId()).isNotNull();

        Long noteBookId = response.getBody().getId();

        ResponseEntity<NoteBook> noteBookDetailResponse = template().getForEntity("/api/notebooks/" + noteBookId, NoteBook.class);

        assertThat(noteBookDetailResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(noteBookDetailResponse.getBody().getTitle()).isEqualTo(noteBookName);
    }

    @Test
    public void getNoteBookWithoutDeletedNote() {
        ResponseEntity<Note> createNoteResponse = basicAuthTemplate().postForEntity("/api/notes/notebook/1", null, Note.class);
        Note testNote = createNoteResponse.getBody();
        Long testNoteId = createNoteResponse.getBody().getId();
        deleteForEntity("/api/notes/" + testNoteId, Void.class);

        ResponseEntity<NoteBook> response = template().getForEntity("/api/notebooks/1", NoteBook.class);
        assertThat(response.getBody().getNotes()).doesNotContain(testNote.delete());
    }

    @Test
    public void getNoteBookNotBlankValidationMessageTest() {
        String noteBookName = "    ";
        NoteBookDto noteBookDto = new NoteBookDto(noteBookName);
        ResponseEntity<ValidationErrorsResponse> response = basicAuthTemplate().postForEntity("/api/notebooks", noteBookDto, ValidationErrorsResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().get(0).getErrorMessage()).isEqualTo(msa.getMessage(ErrorMessage.NOTE_BOOK_NOT_BLANK.getMessageKey()));
    }

    @Test
    public void deleteNoteBookTest() {
        String noteBookName = "내가 쓴 첫번 째 노트북";
        NoteBookDto noteBookDto = new NoteBookDto(noteBookName);
        ResponseEntity<NoteBook> response = basicAuthTemplate(defaultUser()).postForEntity("/api/notebooks", noteBookDto, NoteBook.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        NoteBook createdNoteBook = response.getBody();

        ResponseEntity deleteNoteBookResponse = deleteForEntity("/api/notebooks/" + createdNoteBook.getId(),  Void.class);
        assertThat(deleteNoteBookResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<NoteBook> getNoteBookByNoteBookIdResponse = template().getForEntity("/api/notebooks/" + createdNoteBook.getId(), NoteBook.class);
        assertThat(getNoteBookByNoteBookIdResponse.getBody().isDeleted()).isTrue();
    }

    @Test
    public void deleteNoteBookTestUnAuthorized() {
        String noteBookName = "내가 쓴 첫번 째 노트북";
        NoteBookDto noteBookDto = new NoteBookDto(noteBookName);
        ResponseEntity<NoteBook> response = basicAuthTemplate().postForEntity("/api/notebooks", noteBookDto, NoteBook.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        NoteBook createdNoteBook = response.getBody();

        ResponseEntity<ErrorDetails> deleteNoteBookResponse = deleteForEntity(UserDto.defaultUserDto().setEmail("anotherUser").toEntity(), "/api/notebooks/" + createdNoteBook.getId(),  ErrorDetails.class);
        assertThat(deleteNoteBookResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
