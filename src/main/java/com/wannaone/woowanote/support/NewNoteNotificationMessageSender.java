package com.wannaone.woowanote.support;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.dto.NotificationMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class NewNoteNotificationMessageSender {
    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    public void sendSharedNoteBookCreateNoteNotificationMessage(Note note) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto(note);
        note.getNoteBook().getPeers().stream().forEach((peer) -> {
            String topic = "/topic/users/" + peer.getId();
            simpMessageSendingOperations.convertAndSend(
                    topic,
                    notificationMessageDto
            );
        });
    }
}
