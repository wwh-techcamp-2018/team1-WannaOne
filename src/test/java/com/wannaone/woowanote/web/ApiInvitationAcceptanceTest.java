package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.InvitationAnswerDto;
import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.repository.InvitationRepository;
import com.wannaone.woowanote.service.UserService;
import com.wannaone.woowanote.support.InvitationStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiInvitationAcceptanceTest extends AcceptanceTest {
    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private UserService userService;

    @Test
    public void acceptInvitationStatus() {
        User loginUser = UserDto.defaultUserDto().setEmail("dain@woowahan.com").toEntity();

        InvitationAnswerDto responseDto =  new InvitationAnswerDto(InvitationStatus.ACCEPTED, 3L);
        ResponseEntity<Void> response = basicAuthTemplate(loginUser).postForEntity("/api/invitations", responseDto, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(invitationRepository.findById(3L).get().getStatus()).isEqualTo(InvitationStatus.ACCEPTED);
    }
}
