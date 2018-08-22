package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
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
    public Note save(Long noteBookId, Note note, User writer) {
        NoteBook noteBook = noteBookRepository.findById(noteBookId)
                .orElseThrow(() -> new RecordNotFoundException(msa.getMessage("NotFound.noteBook")));
        note.setNoteBook(noteBook);
        note.setWriter(writer);
        noteRepository.save(note);
        //안 해도 노트와 연관관계가 설정되지만 객체지향 관점에서 명시적으로 표시하는게 좋은 듯.
        noteBook.addNote(note);
        return note;
    }

    public Note updateNote(Long id, Note updateNote) {
        Note originNote = getNote(id);
        return noteRepository.save(originNote.update(updateNote));
    }
}
