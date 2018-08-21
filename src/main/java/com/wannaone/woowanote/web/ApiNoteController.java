package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
public class ApiNoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/{id}")
    public Note show(@PathVariable Long id) {
        return noteService.getNote(id);
    }

    @GetMapping
    public ResponseEntity showAllNotes() {
        return ResponseEntity.ok().body(noteService.getAllNotes());
    }

    @PostMapping("/notebook/{noteBookId}")
    public ResponseEntity<Note> create(@PathVariable Long noteBookId, @RequestBody Note note) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.save(noteBookId, note));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> update(@PathVariable Long id, @RequestBody Note updateNote) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.updateNote(id, updateNote));
    }
}
