package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.dto.NoteBookDto;
import com.wannaone.woowanote.dto.NoteBookTitleDto;
import com.wannaone.woowanote.dto.NoteDto;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiNoteBookAcceptanceTest extends AcceptanceTest {
    @Autowired
    private MessageSourceAccessor msa;
    @Test
    public void showAllNoteBooks() {
        ResponseEntity<List<NoteBookDto>> response =
                getForEntityWithParameterizedWithBasicAuth("/api/notebooks", null, new ParameterizedTypeReference<List<NoteBookDto>>() {});

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
        NoteBookTitleDto noteBookDto = new NoteBookTitleDto(noteBookName);
        ResponseEntity<NoteBook> response = basicAuthTemplate().postForEntity("/api/notebooks", noteBookDto, NoteBook.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getTitle()).isEqualTo(noteBookName);
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    public void getNoteBookByNoteBookId() {
        String noteBookName = "내가 쓴 첫번 째 노트북";
        NoteBookTitleDto noteBookDto = new NoteBookTitleDto(noteBookName);
        ResponseEntity<NoteBook> response = basicAuthTemplate(defaultUser()).postForEntity("/api/notebooks", noteBookDto, NoteBook.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getTitle()).isEqualTo(noteBookName);
        assertThat(response.getBody().getId()).isNotNull();

        Long noteBookId = response.getBody().getId();

        ResponseEntity<NoteBookDto> noteBookDetailResponse = basicAuthTemplate().getForEntity("/api/notebooks/" + noteBookId, NoteBookDto.class);

        assertThat(noteBookDetailResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(noteBookDetailResponse.getBody().getTitle()).isEqualTo(noteBookName);
    }

    @Test
    public void getNoteBookWithoutDeletedNote() {
        ResponseEntity<Note> createNoteResponse = basicAuthTemplate().postForEntity("/api/notes/notebook/1", null, Note.class);
        Note testNote = createNoteResponse.getBody();
        Long testNoteId = createNoteResponse.getBody().getId();
        deleteForEntity("/api/notes/" + testNoteId, Void.class);

        ResponseEntity<NoteBookDto> response = basicAuthTemplate().getForEntity("/api/notebooks/1", NoteBookDto.class);
        assertThat(response.getBody().getNotes()).doesNotContain(NoteDto.fromEntity(testNote.delete()));
    }

    @Test
    public void getNoteBookNotBlankValidationMessageTest() {
        String noteBookName = "    ";
        NoteBookTitleDto noteBookDto = new NoteBookTitleDto(noteBookName);
        ResponseEntity<ValidationErrorsResponse> response = basicAuthTemplate().postForEntity("/api/notebooks", noteBookDto, ValidationErrorsResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteNoteBookTest() {
        String noteBookName = "내가 쓴 첫번 째 노트북";
        NoteBookTitleDto noteBookDto = new NoteBookTitleDto(noteBookName);
        ResponseEntity<NoteBook> response = basicAuthTemplate(defaultUser()).postForEntity("/api/notebooks", noteBookDto, NoteBook.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        NoteBook createdNoteBook = response.getBody();

        ResponseEntity deleteNoteBookResponse = deleteForEntity("/api/notebooks/" + createdNoteBook.getId(),  Void.class);
        assertThat(deleteNoteBookResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<NoteBookDto> getNoteBookByNoteBookIdResponse = basicAuthTemplate().getForEntity("/api/notebooks/" + createdNoteBook.getId(), NoteBookDto.class);
        assertThat(getNoteBookByNoteBookIdResponse.getBody().isDeleted()).isTrue();
    }

    @Test
    public void deleteNoteBookTestUnAuthorized() {
        String noteBookName = "내가 쓴 첫번 째 노트북";
        NoteBookTitleDto noteBookDto = new NoteBookTitleDto(noteBookName);
        ResponseEntity<NoteBook> response = basicAuthTemplate().postForEntity("/api/notebooks", noteBookDto, NoteBook.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        NoteBook createdNoteBook = response.getBody();

        ResponseEntity<ErrorDetails> deleteNoteBookResponse = deleteForEntity(UserDto.defaultUserDto().setEmail("anotherUser").toEntity(), "/api/notebooks/" + createdNoteBook.getId(),  ErrorDetails.class);
        assertThat(deleteNoteBookResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void showAllSharedNoteBookTest() {

        UserDto user = UserDto.defaultUserDto().setEmail("test@woowahan.com");
        ResponseEntity response = template().postForEntity("/api/users", user, Void.class);

        String noteBookName = "내가 쓴 유니크한 노트북!@#$%";
        NoteBookTitleDto noteBookDto = new NoteBookTitleDto(noteBookName);
        ResponseEntity<NoteBook> createNoteBookResponse = basicAuthTemplate(user.toEntity())
                .postForEntity("/api/notebooks", noteBookDto, NoteBook.class);
        Long noteBookId = createNoteBookResponse.getBody().getId();


        ResponseEntity<NoteBook> addSharedResponse = basicAuthTemplate().postForEntity("/api/users/shared/" + noteBookId, null, NoteBook.class);
        assertThat(addSharedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(addSharedResponse.getBody().getId()).isEqualTo(noteBookId);
        assertThat(addSharedResponse.getBody().getPeers().get(0).getEmail()).isEqualTo("doy@woowahan.com");

        ResponseEntity<List<NoteBookDto>> sharedNoteBookResponse = getForEntityWithParameterizedWithBasicAuth("/api/notebooks/all", null, new ParameterizedTypeReference<List<NoteBookDto>>() {});
        assertThat(sharedNoteBookResponse.getBody().stream().map((notebook) -> notebook.getTitle()).collect(Collectors.toList())).contains(noteBookName);
    }
}
