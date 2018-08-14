package com.wannaone.woowanote.web;

import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.service.UserService;
import com.wannaone.woowanote.validation.ValidationMessageUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AcceptanceTest {
    @Autowired
    private UserService userService;

    @Test
    public void userCreateSuccessTest() {
        UserDto user = UserDto.defaultUserDto();
        ResponseEntity response = template().postForEntity("/api/users", user, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(userService.isExistUser(user.getEmail())).isTrue();
    }

    @Test
    public void userCreateFailTest() {
        UserDto user = new UserDto("doy@woowahan.com");
        ResponseEntity response = template().postForEntity("/api/users", user, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isEqualTo(ValidationMessageUtil.USER_DUPLICATION);
    }
}