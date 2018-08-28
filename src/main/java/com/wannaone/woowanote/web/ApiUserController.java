package com.wannaone.woowanote.web;

import com.wannaone.woowanote.common.SessionUtil;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.LoginDto;
import com.wannaone.woowanote.dto.NoteBookTitleDto;
import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.security.LoginUser;
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

    @PostMapping("/shared/{noteBookId}")
    //TODO: 경로랑 함수이름 추천좀..ㅎㅎ
    public ResponseEntity addSharedNotebook(@PathVariable Long noteBookId, @LoginUser User loginUser) {
        return new ResponseEntity(userService.addSharedNoteBook(loginUser, noteBookId), HttpStatus.OK);
    }

    private NoteBookTitleDto getDefaultNoteBooTitlekDto() {
        return new NoteBookTitleDto("나의 우아한 노트북");
    }


    @GetMapping("/search/{searchEmailText}")
    public ResponseEntity search(@PathVariable String searchEmailText, @LoginUser User loginUser) {
        return ResponseEntity.ok().body(userService.searchEmailLike(searchEmailText, loginUser));
    }
}
