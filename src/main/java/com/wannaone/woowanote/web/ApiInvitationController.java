package com.wannaone.woowanote.web;

import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.InvitationAnswerDto;
import com.wannaone.woowanote.security.LoginUser;
import com.wannaone.woowanote.service.InvitationService;
import com.wannaone.woowanote.support.InvitationStatus;
import com.wannaone.woowanote.support.NotificationMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/invitations")
public class ApiInvitationController {
    private static final Logger log = LoggerFactory.getLogger(ApiInvitationController.class);

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private NotificationMessageSender notificationMessageSender;

    @PostMapping
    public ResponseEntity processInvitationStatus(@LoginUser User loginUser, @RequestBody InvitationAnswerDto statusDto) {
        if (statusDto.getResponse() == InvitationStatus.ACCEPTED) {
            notificationMessageSender.sendSharedNoteBookAcceptMessage
                    (invitationService.processInvitationAnswer(loginUser, statusDto));
        } else if (statusDto.getResponse() == InvitationStatus.REJECTED) {
            notificationMessageSender.sendSharedNoteBookRejectMessage
                    (invitationService.processInvitationAnswer(loginUser, statusDto));
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
