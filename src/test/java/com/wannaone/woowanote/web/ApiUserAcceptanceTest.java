package com.wannaone.woowanote.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wannaone.woowanote.dto.LoginDto;
import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.service.UserService;
import com.wannaone.woowanote.validation.ValidationErrorsResponse;
import com.wannaone.woowanote.validation.ValidationMessageUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 테스트 메소드는 production 코드에서 exception을 던질수 있기 때문에 웬만하면 throws Exception을 추가하는게 좋다고 함.
 */
public class ApiUserAcceptanceTest extends AcceptanceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSourceAccessor msa;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void userCreateSuccessTest() throws Exception {
        UserDto user = UserDto.defaultUserDto();
        ResponseEntity response = template().postForEntity("/api/users", user, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(userService.isExistUser(user.getEmail())).isTrue();
    }

    @Test
    public void userCreateFailTest() throws Exception {
        UserDto user = new UserDto("doy@woowahan.com");
        ResponseEntity response = template().postForEntity("/api/users", user, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isEqualTo(ValidationMessageUtil.USER_DUPLICATION);
    }

    @Test
    public void loginSuccessTest() throws Exception {
        LoginDto loginDto = LoginDto.defaultLoginDto();
        ResponseEntity response = template().postForEntity("/api/users/login", loginDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void loginFailureTestNotEmail() throws Exception {
        LoginDto loginDto = LoginDto.defaultLoginDto().setEmail("notemail");
        ResponseEntity<ValidationErrorsResponse> response = template().postForEntity("/api/users/login", loginDto, ValidationErrorsResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().get(0).getErrorMessage()).isEqualTo(msa.getMessage("Email.userDto.email"));
    }

    @Test
    public void loginFailureTestShortPassword() throws Exception {
        LoginDto loginDto = LoginDto.defaultLoginDto().setPassword("12");
        ResponseEntity<ValidationErrorsResponse> response = template().postForEntity("/api/users/login", loginDto, ValidationErrorsResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().get(0).getErrorMessage()).isEqualTo("비밀번호는 4자 이상, 30자 이하이어야 합니다.");
    }
}