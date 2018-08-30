package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.NoteBookTitleDto;
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
        return new ResponseEntity(noteBookService.getNoteBookDtosByOwnerId(loginUser), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity showNoteBookAndSharedNoteBook(@LoginUser User loginUser) {
        return new ResponseEntity(noteBookService.getNoteBookAndSharedNoteBook(loginUser), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid NoteBookTitleDto noteBookDto, @LoginUser User owner) {
        return new ResponseEntity(noteBookService.save(noteBookDto, owner), HttpStatus.CREATED);
    }

    @DeleteMapping("/{noteBookId}")
    public ResponseEntity delete(@PathVariable Long noteBookId, @LoginUser User owner) {
        noteBookService.delete(noteBookId, owner);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{noteBookId}")
    public ResponseEntity getNoteBook(@PathVariable Long noteBookId, @LoginUser User loginUser) {
        return new ResponseEntity(noteBookService.getNoteBookDtoById(noteBookId, loginUser), HttpStatus.OK);
    }
}
