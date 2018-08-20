package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Comment;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.dto.CommentDto;
import com.wannaone.woowanote.repository.CommentRepository;
import com.wannaone.woowanote.repository.NoteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CommentServiceTest {

    @Mock
    private NoteRepository noteRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;

    @Test
    public void createTest() {
        CommentDto commentDto = new CommentDto("test comment");
        Note note = new Note(1L, "1", "1");
        when(noteRepository.findById(1L)).thenReturn(Optional.ofNullable(note));
        commentService.save(commentDto, note.getId());
        Comment comment = commentDto.toEntity(note);
        verify(commentRepository).save(comment);
    }
}