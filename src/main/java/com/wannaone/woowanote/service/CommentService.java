package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Comment;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.dto.CommentDto;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import com.wannaone.woowanote.repository.CommentRepository;
import com.wannaone.woowanote.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private MessageSourceAccessor msa;

    public Comment save(CommentDto commentDto, Long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new RecordNotFoundException(msa.getMessage("NotFound.note")));
        return commentRepository.save(commentDto.toEntity(note));
    }
}
