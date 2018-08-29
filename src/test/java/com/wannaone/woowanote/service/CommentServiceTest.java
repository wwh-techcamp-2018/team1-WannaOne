package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Comment;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.CommentDto;
import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.repository.CommentRepository;
import com.wannaone.woowanote.repository.NoteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CommentServiceTest {
    @Mock
    private NoteService noteService;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;

    @Test
    public void createTest() throws Exception {
        CommentDto commentDto = new CommentDto("test comment");
        User user = UserDto.defaultUserDto().toEntity();
        Note note = new Note("title", "text");
        when(noteService.getNote(1L)).thenReturn(note);
        Comment comment = commentDto.toEntity(note);
        comment.addWriter(user);
        when(commentRepository.save(commentDto.toEntity(note))).thenReturn(comment);
        assertThat(commentService.save(commentDto, 1L, user)).isEqualTo(CommentDto.fromEntity(comment, user));
    }

    @Test
    public void showTest() throws Exception {
        Note note = new Note("title", "text");
        Comment comment1 = new Comment("comment1", note);
        Comment comment2 = new Comment("comment2", note);
        when(commentRepository.findByNoteIdAndDeletedFalse(1L)).thenReturn(Arrays.asList(comment1,comment2));
        assertThat(commentService.getCommentsByNoteId(1L)).contains(comment1, comment2);
    }

    @Test
    public void deleteCommentTest() {
        Note note = new Note("title", "text");
        Comment comment = new Comment("comment", note);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        assertThat(commentService.delete(1L).isDeleted()).isEqualTo(true);
    }
}