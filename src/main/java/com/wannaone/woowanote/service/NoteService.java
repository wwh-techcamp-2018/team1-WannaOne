package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import com.wannaone.woowanote.repository.NoteBookRepository;
import com.wannaone.woowanote.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteBookRepository noteBookRepository;

    @Autowired
    private MessageSourceAccessor msa;

    public Note getNote(Long id) {
       return noteRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("일치하는 노트가 없습니다."));
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Transactional
    public Note postNewNote(Long noteBookId, Note note) {
        NoteBook noteBook = noteBookRepository.findById(noteBookId)
                .orElseThrow(() -> new RecordNotFoundException(msa.getMessage("NotFound.noteBook")));
        noteBook.addNote(note);
        return note;
    }
}
