package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.NoteBookDto;
import com.wannaone.woowanote.security.LoginUser;
import com.wannaone.woowanote.service.NoteBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/notebooks")
public class ApiNoteBookController {
    @Autowired
    private NoteBookService noteBookService;

    @GetMapping
    public ResponseEntity showAll(@LoginUser User loginUser) {
        return new ResponseEntity(noteBookService.getNoteBooksByOwnerId(loginUser.getId()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid NoteBookDto noteBookDto, @LoginUser User owner) {
        return new ResponseEntity(noteBookService.save(noteBookDto, owner), HttpStatus.CREATED);
    }

    @GetMapping("/{noteBookId}")
    public ResponseEntity getNoteBook(@PathVariable Long noteBookId) {
        return new ResponseEntity(noteBookService.getNoteBookByNoteBookId(noteBookId), HttpStatus.OK);
    }
}
