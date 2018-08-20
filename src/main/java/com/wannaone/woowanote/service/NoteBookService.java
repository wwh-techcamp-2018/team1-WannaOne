package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.repository.NoteBookRepository;
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

    public List<NoteBook> getAllNoteBooks() {
        return noteBookRepository.findAll();
    }

    public NoteBook save(NoteBook noteBook) {
        return noteBookRepository.save(noteBook);
    }
}
