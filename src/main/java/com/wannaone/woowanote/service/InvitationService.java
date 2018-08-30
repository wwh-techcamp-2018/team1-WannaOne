package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Invitation;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.InvitationAnswerDto;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import com.wannaone.woowanote.repository.InvitationRepository;
import com.wannaone.woowanote.support.ErrorMessage;
import com.wannaone.woowanote.support.InvitationStatus;
import com.wannaone.woowanote.support.NotificationMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvitationService {
    private static final Logger log = LoggerFactory.getLogger(InvitationService.class);

    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSourceAccessor msa;
    @Autowired
    private NotificationMessageSender notificationMessageSender;

    @Transactional
    public Invitation processInvitationAnswer(User loginUser, InvitationAnswerDto responseDto) {
        Invitation invitation = getInvitationById(responseDto.getInvitationId());
        invitation.setStatus(responseDto.getResponse());
        if (responseDto.getResponse() == InvitationStatus.ACCEPTED) {
            acceptInvitation(loginUser, invitation.getNoteBook().getId());
            notificationMessageSender.sendSharedNoteBookAcceptMessage(invitation);
        } else if (responseDto.getResponse() == InvitationStatus.REJECTED) {
            notificationMessageSender.sendSharedNoteBookRejectMessage(invitation);
        }
        return invitation;
    }

    public void acceptInvitation(User loginUser, Long noteBookId) {
        userService.addSharedNoteBook(loginUser, noteBookId);
    }

    public Invitation getInvitationById(Long id) {
        return invitationRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(msa.getMessage(ErrorMessage.INVITATION_NOT_FOUND.getMessageKey())));
    }

    public List<Invitation> getInvitationsByGuestId(Long id) {
        return invitationRepository.findByGuestId(id);
    }

    public List<Invitation> getInvitationsByNoteBookId(Long id) {
        return invitationRepository.findByNoteBookId(id);
    }

    public Invitation save(Invitation invitation) {
        return invitationRepository.save(invitation);
    }

}
