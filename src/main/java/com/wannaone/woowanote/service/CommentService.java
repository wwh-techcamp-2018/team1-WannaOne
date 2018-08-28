package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Comment;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.CommentDto;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import com.wannaone.woowanote.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NoteService noteService;

    @Autowired
    private MessageSourceAccessor msa;

    @Transactional
    public Comment save(CommentDto commentDto, Long noteId, User loginUser) {
        Note note = noteService.getNote(noteId);
        Comment comment = commentRepository.save(commentDto.toEntity(note));
        note.addComment(comment);
        //Transactional을 명시해주지 않으면 writer 정보가 제대로 들어가지 않는다.
        comment.addWriter(loginUser);
        return comment;
    }

    public List<Comment> getCommentsByNoteId(Long noteId) {
        return commentRepository.findByNoteIdAndDeletedFalse(noteId);
    }

    public Comment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(msa.getMessage("NotFound.comment")));
    }

    @Transactional
    public Comment delete(Long id) {
        return getComment(id).delete();
    }
}
