package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Comment;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.dto.CommentDto;
import com.wannaone.woowanote.repository.CommentRepository;
import com.wannaone.woowanote.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private NoteService noteService;

    public Comment save(CommentDto commentDto, Long noteId) {
        Note note = noteService.getNote(noteId);
        Comment comment = commentRepository.save(commentDto.toEntity(note));
        note.addComment(comment);
        return comment;
    }

    public List<Comment> getCommentsByNoteId(Long noteId) {
        return commentRepository.findByNoteId(noteId);
    }
}
