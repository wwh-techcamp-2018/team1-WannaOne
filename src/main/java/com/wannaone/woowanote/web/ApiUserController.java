package com.wannaone.woowanote.web;

import com.wannaone.woowanote.common.SessionUtil;
import com.wannaone.woowanote.dto.LoginDto;
import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid UserDto userDto) {
        userService.save(userDto);
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


}
