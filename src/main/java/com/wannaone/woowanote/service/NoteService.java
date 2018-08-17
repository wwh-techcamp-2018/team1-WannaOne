package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import com.wannaone.woowanote.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Note getNote(Long id) {
       return noteRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("일치하는 노트가 없습니다."));
    }

    public Long postNewNote(Note note) {
        return noteRepository.save(note).getId();
    }
}
