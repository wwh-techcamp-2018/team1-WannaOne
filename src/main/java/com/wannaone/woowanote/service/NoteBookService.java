package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import com.wannaone.woowanote.repository.NoteBookRepository;
import com.wannaone.woowanote.support.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteBookService {
    @Autowired
    private NoteBookRepository noteBookRepository;
    @Autowired
    private MessageSourceAccessor msa;

    public List<NoteBook> getNoteBooksByOwnerId(Long ownerId) {
        return noteBookRepository.findByOwnerId(ownerId);
    }

    public NoteBook save(NoteBook noteBook, User owner) {
        //owner는 영속성 컨텍스트에 포함되지 않음. 근데 id값을 가지고 있어서 정상적으로 save 되는 것 같음.
        noteBook.setOwner(owner);
        return noteBookRepository.save(noteBook);
    }

    public NoteBook getNoteBookByNoteBookId(Long noteBookId) {
        return noteBookRepository.findById(noteBookId).orElseThrow(() -> new RecordNotFoundException(msa.getMessage(ErrorMessage.NOTE_BOOK_NOT_FOUND.getMessageKey())));
    }
}
