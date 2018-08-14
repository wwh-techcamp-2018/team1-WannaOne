package com.wannaone.woowanote.web;

import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    private ResponseEntity create(@RequestBody @Valid UserDto userDto) {
        userService.save(userDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
