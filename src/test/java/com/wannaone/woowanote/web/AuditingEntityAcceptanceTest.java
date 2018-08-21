package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.Comment;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.dto.CommentDto;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.assertj.core.api.Assertions.assertThat;


public class AuditingEntityAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(AuditingEntityAcceptanceTest.class);

    @Test
    public void createCommentRegisterDatetimeExistTest() {
        String noteBookName = "내가 쓴 첫번 째 노트북";
        NoteBook noteBook = new NoteBook(noteBookName);
        ResponseEntity<NoteBook> createNoteBookResponse = template().postForEntity("/api/notebooks", noteBook, NoteBook.class);
        Long noteBookId = createNoteBookResponse.getBody().getId();
        assertThat(createNoteBookResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createNoteBookResponse.getBody().getTitle()).isEqualTo(noteBookName);
        assertThat(noteBookId).isNotNull();

        String noteTitle = "내가 쓴 첫번 째 노트";
        Note postNote = new Note(noteTitle, "우아노트는 21세기 현대인을 위한 최고의 노트입니다.");
        ResponseEntity<Note> createNoteResponse = template().postForEntity("/api/notes/notebook/" + noteBookId, postNote, Note.class);
        Long noteId = createNoteResponse.getBody().getId();
        assertThat(createNoteResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createNoteResponse.getBody().getTitle()).isEqualTo(noteTitle);
        assertThat(noteId).isNotNull();

        String commentContent = "댓글 내용";
        CommentDto commentDto = new CommentDto(commentContent);
        ResponseEntity<Comment> commentCreateResponse = template().postForEntity("/api/notes/" + noteId + "/comments", commentDto, Comment.class);
        Comment comment = commentCreateResponse.getBody();
        assertThat(commentCreateResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(comment.getContent()).isEqualTo(commentContent);
        assertThat(comment.getId()).isNotNull();

        log.info("registerDatetime, {}", comment.getRegisterDatetime());
        log.info("updateDatetime, {}", comment.getUpdateDatetime());
        assertThat(comment.getRegisterDatetime()).isNotNull();
        assertThat(comment.getUpdateDatetime()).isNotNull();
    }

    @Test
    public void createNoteRegisterDatetimeExistTest() {
        String noteBookName = "내가 쓴 첫번 째 노트북";
        NoteBook noteBook = new NoteBook(noteBookName);
        ResponseEntity<NoteBook> createNoteBookResponse = template().postForEntity("/api/notebooks", noteBook, NoteBook.class);
        Long noteBookId = createNoteBookResponse.getBody().getId();
        assertThat(createNoteBookResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createNoteBookResponse.getBody().getTitle()).isEqualTo(noteBookName);
        assertThat(noteBookId).isNotNull();

        String noteTitle = "내가 쓴 첫번 째 노트";
        Note postNote = new Note(noteTitle, "우아노트는 21세기 현대인을 위한 최고의 노트입니다.");
        ResponseEntity<Note> createNoteResponse = template().postForEntity("/api/notes/notebook/" + noteBookId, postNote, Note.class);
        Long noteId = createNoteResponse.getBody().getId();
        assertThat(createNoteResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createNoteResponse.getBody().getTitle()).isEqualTo(noteTitle);
        assertThat(noteId).isNotNull();
        Note note = createNoteResponse.getBody();

        log.info("registerDatetime, {}", note.getRegisterDatetime());
        log.info("updateDatetime, {}", note.getUpdateDatetime());
        assertThat(note.getRegisterDatetime()).isNotNull();
        assertThat(note.getUpdateDatetime()).isNotNull();
    }
}