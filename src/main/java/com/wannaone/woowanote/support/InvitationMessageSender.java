package com.wannaone.woowanote.support;

import com.wannaone.woowanote.domain.Invitation;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.NotificationMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class InvitationMessageSender {
    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    public void sendPersonalMessage(User user, Invitation invitation) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto(invitation);
        String topic = "/topic/users/" + user.getId();
        simpMessageSendingOperations.convertAndSend(
                topic,
                notificationMessageDto
        );
    }
}
