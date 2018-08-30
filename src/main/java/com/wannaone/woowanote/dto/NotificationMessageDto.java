package com.wannaone.woowanote.dto;

import com.wannaone.woowanote.domain.Invitation;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.support.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationMessageDto {
    private NotificationType type;
    private Long id;
    private String message;

    public static NotificationMessageDto getWriteNotificationMessage(Note note) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setId(note.getNoteBook().getId());
        notificationMessageDto.setType(NotificationType.WRITE_NOTIFICATION);
        String message = note.getWriter().getName() + "님이 " + note.getNoteBook().getTitle() + "에 새 노트를 작성했습니다.";
        notificationMessageDto.setMessage(message);
        return notificationMessageDto;
    }

    public static NotificationMessageDto getInvitationMessge(Invitation invitation) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setId(invitation.getId());
        notificationMessageDto.setType(NotificationType.INVITATION);
        String message = invitation.getHost().getName() +"님의 " + invitation.getNoteBook().getTitle() + " 공유 초대";
        notificationMessageDto.setMessage(message);
        return notificationMessageDto;
    }

    public static NotificationMessageDto getAcceptMessage(Invitation invitation) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setId(invitation.getId());
        notificationMessageDto.setType(NotificationType.ACCEPT);
        String message = invitation.getGuest() + "님이 " + invitation.getNoteBook().getTitle() + "초대를 수락했습니다.";
        notificationMessageDto.setMessage(message);
        return notificationMessageDto;
    }

    public NotificationMessageDto() {
    }
}
