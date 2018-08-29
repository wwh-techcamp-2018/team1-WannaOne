package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Invitation;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.InvitationAnswerDto;
import com.wannaone.woowanote.repository.InvitationRepository;
import com.wannaone.woowanote.support.InvitationStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvitationServiceTest {
    @Mock
    private InvitationRepository invitationRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private InvitationService invitationService;


    @Test
    public void processInvitationStatusTest() {
        User loginUser = new User(1L, "doy@woowahan.com", "1234");
        User host = new User(2L, "dain@woowahan.com", "1234");
        NoteBook notebook = new NoteBook(1L, host, "sharing_notebook");
        Invitation invitation = new Invitation(host, loginUser, notebook,InvitationStatus.PENDING);
        InvitationAnswerDto invitationAnswerDto = new InvitationAnswerDto(InvitationStatus.ACCEPTED, 1L);

        when(invitationRepository.findById(1L)).thenReturn(Optional.of(invitation));
        Invitation invitationResult = invitationService.processInvitationAnswer(loginUser, invitationAnswerDto);

        assertThat(invitationResult.getStatus()).isEqualTo(InvitationStatus.ACCEPTED);
    }
}