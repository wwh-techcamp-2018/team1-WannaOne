package com.wannaone.woowanote.web;

import com.wannaone.woowanote.dto.InvitationResponseDto;
import com.wannaone.woowanote.repository.InvitationRepository;
import com.wannaone.woowanote.support.InvitationStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiInvitationAcceptanceTest extends AcceptanceTest {
    @Autowired
    private InvitationRepository invitationRepository;
    @Test
    public void acceptInvitationStatus() {
        InvitationResponseDto responseDto =  new InvitationResponseDto(InvitationStatus.ACCEPTED, 1L);
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity("/api/invitations", responseDto, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(invitationRepository.findById(1L).get().getStatus()).isEqualTo(InvitationStatus.ACCEPTED);
    }
}
