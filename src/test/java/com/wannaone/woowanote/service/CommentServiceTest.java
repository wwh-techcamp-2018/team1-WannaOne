package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Comment;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.dto.CommentDto;
import com.wannaone.woowanote.repository.CommentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
    public void createTest() {
        CommentDto commentDto = new CommentDto("test comment");
        Note note = new Note("title", "text");
        when(noteService.getNote(1L)).thenReturn(note);
        commentService.save(commentDto, 1L);
        Comment comment = commentDto.toEntity(note);
        verify(commentRepository).save(comment);
    }
}