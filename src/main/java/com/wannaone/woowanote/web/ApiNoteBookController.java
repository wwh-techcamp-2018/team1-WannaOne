package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.service.NoteBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notebooks")
public class ApiNoteBookController {
    @Autowired
    private NoteBookService noteBookService;

    @GetMapping
    public ResponseEntity showAll() {
        return new ResponseEntity(noteBookService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody NoteBook noteBook) {
        return new ResponseEntity(noteBookService.save(noteBook), HttpStatus.CREATED);
    }
}
