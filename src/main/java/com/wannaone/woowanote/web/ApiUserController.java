package com.wannaone.woowanote.web;

import com.wannaone.woowanote.common.SessionUtil;
import com.wannaone.woowanote.dto.LoginDto;
import com.wannaone.woowanote.dto.NoteBookTitleDto;
import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.dto.*;
import com.wannaone.woowanote.service.NoteBookService;
import com.wannaone.woowanote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private NoteBookService noteBookService;

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid UserDto userDto) {
        noteBookService.save(getDefaultNoteBooTitlekDto(), userService.save(userDto));
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDto loginDto, HttpSession session) {
        SessionUtil.setUser(session, userService.login(loginDto));
        return new ResponseEntity(HttpStatus.OK);

    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpSession session) {
        SessionUtil.logout(session);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/invite")
    public InvitationGuestDto precheckInvitation(InvitationPrecheckingDto precheckingDto) {
        return userService.precheckInvitationValidity(precheckingDto);
    }

    private NoteBookTitleDto getDefaultNoteBooTitlekDto() {
        return new NoteBookTitleDto("나의 우아한 노트북");
    }

}
