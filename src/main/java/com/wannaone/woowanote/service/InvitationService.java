package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Invitation;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.InvitationResponseDto;
import com.wannaone.woowanote.dto.NoteBookDto;
import com.wannaone.woowanote.repository.InvitationRepository;
import com.wannaone.woowanote.support.InvitationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationService {
    private static final Logger log = LoggerFactory.getLogger(InvitationService.class);

    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private NoteBookService noteBookservice;


    public Invitation processInvitationResponse(User loginUser, InvitationResponseDto responseDto) {
        Invitation invitation = invitationRepository.findById(responseDto.getInvitationId()).get();
        NoteBookDto noteBookDto = NoteBookDto.fromEntity(invitation.getNoteBook());
        if (responseDto.getResponse() == InvitationStatus.ACCEPTED) {
            acceptInvitation(loginUser, noteBookDto.getId());
        }
        //TODO:다른 status도 처리
        invitation.setStatus(responseDto.getResponse());
        invitationRepository.save(invitation);
        return invitation;
    }

    public void acceptInvitation(User loginUser, Long noteBookId) {
        userService.addSharedNoteBook(loginUser, noteBookId);
    }


}
