package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.Comment;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.dto.CommentDto;
import com.wannaone.woowanote.dto.NoteBookTitleDto;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiCommentAcceptanceTest extends AcceptanceTest {
    @Test
    public void createComment() throws Exception {
        String noteBookName = "내가 쓴 첫번 째 노트북";
        NoteBookTitleDto noteBookDto  = new NoteBookTitleDto(noteBookName);
        ResponseEntity<NoteBook> createNoteBookResponse = basicAuthTemplate().postForEntity("/api/notebooks", noteBookDto, NoteBook.class);
        Long noteBookId = createNoteBookResponse.getBody().getId();
        assertThat(createNoteBookResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createNoteBookResponse.getBody().getTitle()).isEqualTo(noteBookName);
        assertThat(noteBookId).isNotNull();

        ResponseEntity<Note> createNoteResponse = basicAuthTemplate().postForEntity("/api/notes/notebook/" + noteBookId, null, Note.class);
        Long noteId = createNoteResponse.getBody().getId();
        assertThat(createNoteResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createNoteResponse.getBody().getTitle()).isEqualTo("나의 우아한 노트");
        assertThat(noteId).isNotNull();

        String commentContent = "댓글 내용";
        CommentDto commentDto = new CommentDto(commentContent);
        ResponseEntity<Comment> commentCreateResponse = basicAuthTemplate().postForEntity("/api/notes/" + noteId + "/comments", commentDto, Comment.class);
        assertThat(commentCreateResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(commentCreateResponse.getBody().getContent()).isEqualTo(commentContent);
        assertThat(commentCreateResponse.getBody().getId()).isNotNull();
    }

    @Test
    public void createCommentNoteNotFound() throws Exception {
        String commentContent = "댓글 내용";
        CommentDto commentDto = new CommentDto(commentContent);
        ResponseEntity commentCreateResponse = basicAuthTemplate().postForEntity("/api/notes/-1/comments", commentDto, Void.class);
        assertThat(commentCreateResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void show() throws Exception {
        ResponseEntity<List<Comment>> commentShowResponse = getForEntityWithParameterized("/api/notes/1/comments", null, new ParameterizedTypeReference<List<Comment>>() {});
        assertThat(commentShowResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(commentShowResponse.getBody().get(0).getContent()).contains("댓글");
    }
}
