package com.wannaone.woowanote.support;

import com.wannaone.woowanote.domain.Invitation;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.NotificationMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMessageSender {
    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    public void sendSharedNoteBookAcceptMessage(Invitation invitation) {
        NotificationMessageDto notificationMessageDto = NotificationMessageDto.getAcceptMessage(invitation);
        String topic = "/topic/users/" + invitation.getHost().getId();
        simpMessageSendingOperations.convertAndSend(
                topic,
                notificationMessageDto
        );
    }

    public void sendSharedNoteBookRejectMessage(Invitation invitation) {
        NotificationMessageDto notificationMessageDto = NotificationMessageDto.getRejectMessage(invitation);
        String topic = "/topic/users/" + invitation.getHost().getId();
        simpMessageSendingOperations.convertAndSend(
                topic,
                notificationMessageDto
        );
    }

    public void sendSharedNoteBookCreateNoteNotificationMessage(Note note) {
        NotificationMessageDto notificationMessageDto = NotificationMessageDto.getWriteNotificationMessage(note);
        List<User> subscribers = note.getNoteBook().getPeers().stream().collect(Collectors.toList());
        subscribers.add(note.getNoteBook().getOwner());
        subscribers.stream().forEach((subscriber) -> {
            String topic = "/topic/users/" + subscriber.getId();
            simpMessageSendingOperations.convertAndSend(
                    topic,
                    notificationMessageDto
            );
        });
    }

    public void sendInvitationMessage(User user, Invitation invitation) {
        NotificationMessageDto notificationMessageDto = NotificationMessageDto.getInvitationMessge(invitation);
        String topic = "/topic/users/" + user.getId();
        simpMessageSendingOperations.convertAndSend(
                topic,
                notificationMessageDto
        );
    }
}
