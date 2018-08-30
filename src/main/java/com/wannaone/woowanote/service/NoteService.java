package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import com.wannaone.woowanote.repository.NoteRepository;
import com.wannaone.woowanote.support.NewNoteNotificationMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    private static final Logger log = LoggerFactory.getLogger(NoteService.class);

    @Autowired
    private NewNoteNotificationMessageSender newNoteNotificationMessageSender;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteBookService noteBookService;

    @Autowired
    private MessageSourceAccessor msa;

    public Note getNote(Long id) {
        return noteRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new RecordNotFoundException(msa.getMessage("NotFound.note")));
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }
    
    public Note save(Long noteBookId, User writer) {
        NoteBook noteBook = noteBookService.getNoteBookById(noteBookId);
        Note newNote = new Note(writer);
        newNote.addNoteBook(noteBook);
        noteRepository.save(newNote);
        //안 해도 노트와 연관관계가 설정되지만 객체지향 관점에서 명시적으로 표시하는게 좋은 듯.
        noteBook.addNote(newNote);
        log.debug("saving new note. noteBookId: {}, writer.name: {}",
                noteBookId, Optional.ofNullable(writer).orElse(User.defaultUser()).getEmail());
        newNoteNotificationMessageSender.sendSharedNoteBookCreateNoteNotificationMessage(newNote);
        return newNote;
    }

    @Transactional
    public Note updateNote(Long id, Note updateNote) {
        return getNote(id).update(updateNote);
    }

    @Transactional
    public Note deleteNote(Long id) {
        return getNote(id).delete();
    }

    @Transactional
    public Note updateNoteWithParentNoteBook(Long noteId, Long noteBookId) {
        Note updateNote = getNote(noteId);
        NoteBook prevParentNoteBook = updateNote.getNoteBook();
        NoteBook newParentNoteBook = noteBookService.getNoteBookById(noteBookId);
        prevParentNoteBook.removeNote(updateNote);
        newParentNoteBook.addNote(updateNote);
        updateNote.updateNoteBook(newParentNoteBook);
        return updateNote;
    }
}
