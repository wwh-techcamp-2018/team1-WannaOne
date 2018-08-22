package com.wannaone.woowanote.web;

import com.wannaone.woowanote.common.SessionUtil;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.exception.UnAuthenticationException;
import com.wannaone.woowanote.service.NoteBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/notebooks")
public class ApiNoteBookController {
    @Autowired
    private NoteBookService noteBookService;

    @Autowired
    private MessageSourceAccessor msa;

    @GetMapping
    public ResponseEntity showAll(HttpSession session) {
        User loginUser = SessionUtil.getUser(session).orElseThrow(() -> new UnAuthenticationException(msa.getMessage("unauthentication.not.logined")));
        return new ResponseEntity(noteBookService.getNoteBooksByOwnerId(loginUser.getId()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody NoteBook noteBook) {
        return new ResponseEntity(noteBookService.save(noteBook), HttpStatus.CREATED);
    }
}
