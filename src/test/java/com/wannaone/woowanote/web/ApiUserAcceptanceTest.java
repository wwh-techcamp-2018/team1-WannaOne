package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.*;
import com.wannaone.woowanote.exception.ErrorDetails;
import com.wannaone.woowanote.service.UserService;
import com.wannaone.woowanote.support.ErrorMessage;
import com.wannaone.woowanote.validation.ValidationErrorsResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 테스트 메소드는 production 코드에서 exception을 던질수 있기 때문에 웬만하면 throws Exception을 추가하는게 좋다고 함.
 */
public class ApiUserAcceptanceTest extends AcceptanceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageSourceAccessor msa;

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
        ResponseEntity<ErrorDetails> response = template().postForEntity("/api/users", user, ErrorDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody().getMessage()).isEqualTo(msa.getMessage("email.duplicate.message"));
    }

    @Test
    public void userCreateFailTestWithNotEmail() throws Exception {
        UserDto user = new UserDto("doy");
        ResponseEntity<ValidationErrorsResponse> response = template().postForEntity("/api/users", user, ValidationErrorsResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().get(0).getErrorMessage()).isEqualTo(msa.getMessage(ErrorMessage.EMAIL_NOT_VALID.getMessageKey()));
    }

    @Test
    public void loginSuccessTest() throws Exception {
        LoginDto loginDto = LoginDto.defaultLoginDto();
        ResponseEntity response = template().postForEntity("/api/users/login", loginDto, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void loginFailureTestNotEmail() throws Exception {
        LoginDto loginDto = LoginDto.defaultLoginDto().setEmail("notemail");
        ResponseEntity<ValidationErrorsResponse> response = template().postForEntity("/api/users/login", loginDto, ValidationErrorsResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().get(0).getErrorMessage()).isEqualTo(msa.getMessage("Email.loginDto.email"));
    }

    @Test
    public void loginFailureTestShortPassword() throws Exception {
        LoginDto loginDto = LoginDto.defaultLoginDto().setPassword("12");
        ResponseEntity<ValidationErrorsResponse> response = template().postForEntity("/api/users/login", loginDto, ValidationErrorsResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().get(0).getErrorMessage()).isEqualTo("비밀번호는 4자 이상, 15자 이하이어야 합니다.");
    }

    @Test
    public void logoutTest() throws Exception {
        ResponseEntity response = basicAuthTemplate().postForEntity("/api/users/logout", null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void inviteTest() throws Exception {
        Long guestId = 2L;
        Long notebookId = 1L;
        List<Long> guestIdList = Arrays.asList(guestId);
        InvitationDto invitationDto = new InvitationDto(guestIdList, notebookId);
        ResponseEntity response = basicAuthTemplate().postForEntity("/api/users/invite", invitationDto, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void precheckInvitationTest() throws Exception {
        ResponseEntity<InvitationGuestDto> response = basicAuthTemplate().getForEntity("/api/users/invite?guestEmail=dain@woowahan.com&noteBookId=1", InvitationGuestDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("dain");
    }

    @Test
    public void searchLikeUserNameTest() {
        String userEmail = "abcdefg@naver.com";
        UserDto user = UserDto.defaultUserDto().setEmail(userEmail);
        ResponseEntity createUserResponse = template().postForEntity("/api/users", user, Void.class);
        assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<List<InvitationGuestDto>> searchLikeUserNameResponse = getForEntityWithParameterizedWithBasicAuth("/api/users/search/abcdefg", null, new ParameterizedTypeReference<List<InvitationGuestDto>>() {});
        assertThat(searchLikeUserNameResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<String> emails = searchLikeUserNameResponse.getBody().stream().map((userInfo) -> user.getEmail()).collect(Collectors.toList());
        assertThat(emails).contains(userEmail);
    }

    @Test
    public void showInvitationTest() throws Exception {
        Long guestId = 1L;
        Long notebookId = 7L;
        List<Long> guestIdList = Arrays.asList(guestId);
        InvitationDto invitationDto = new InvitationDto(guestIdList, notebookId);

        User host = UserDto.defaultUserDto().setEmail("dain@woowahan.com").toEntity();
        basicAuthTemplate(host).postForEntity("/api/users/invite", invitationDto, Void.class);

        ResponseEntity<List<NotificationMessageDto>> invitationListResponse = getForEntityWithParameterizedWithBasicAuth("/api/users/invitations", null, new ParameterizedTypeReference<List<NotificationMessageDto>>() {});
        assertThat(invitationListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(invitationListResponse.getBody().size()).isEqualTo(3);
    }
}