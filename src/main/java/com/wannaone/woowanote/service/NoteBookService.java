package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import com.wannaone.woowanote.exception.UnAuthorizedException;
import com.wannaone.woowanote.repository.NoteBookRepository;
import com.wannaone.woowanote.support.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteBookService {
    @Autowired
    private NoteBookRepository noteBookRepository;
    @Autowired
    private MessageSourceAccessor msa;

    public List<NoteBook> getNoteBooksByOwnerId(Long ownerId) {
        return noteBookRepository.findByOwnerIdAndDeletedFalse(ownerId);
    }

    @Transactional
    public NoteBook save(NoteBook noteBook, User owner) {
        noteBook.setOwner(owner);
        return noteBookRepository.save(noteBook);
    }

    public NoteBook getNoteBookByNoteBookId(Long noteBookId) {
        return noteBookRepository.findById(noteBookId).orElseThrow(() -> new RecordNotFoundException(msa.getMessage(ErrorMessage.NOTE_BOOK_NOT_FOUND.getMessageKey())));
    }

    @Transactional
    public NoteBook delete(Long noteBookId, User owner) {
        NoteBook deleteNoteBook = getNoteBookByNoteBookId(noteBookId);
        if(!deleteNoteBook.getOwner().equalsForNonPersistenceEntity(owner)) {
            throw new UnAuthorizedException(msa.getMessage(ErrorMessage.UNAUTHORIZED.getMessageKey()));
        }
        deleteNoteBook.delete();
        return deleteNoteBook;
    }
}
