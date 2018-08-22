package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.security.LoginUser;
import com.wannaone.woowanote.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/api/notes")
public class ApiNoteController {
    private static final Logger log = LoggerFactory.getLogger(ApiNoteController.class);

    @Autowired
    private NoteService noteService;

    @Autowired
    private MessageSourceAccessor msa;

    @GetMapping("/{id}")
    public Note show(@PathVariable Long id) {
        return noteService.getNote(id);
    }

    @GetMapping
    public ResponseEntity showAllNotes() {
        return ResponseEntity.ok().body(noteService.getAllNotes());
    }

    @PostMapping("/notebook/{noteBookId}")
    public ResponseEntity<Note> create(@LoginUser User writer, @PathVariable Long noteBookId, @RequestBody Note note, HttpSession session) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.save(noteBookId, note, writer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> update(@PathVariable Long id, @RequestBody Note updateNote) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.updateNote(id, updateNote));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok().build();
    }
}
