package com.wannaone.woowanote.dto;

import com.wannaone.woowanote.domain.Invitation;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.support.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@AllArgsConstructor
public class NotificationMessageDto {
    private NotificationType type;
    private Long id;
    private String message;

    public NotificationMessageDto(Invitation invitation) {
        this.id = invitation.getId();
        this.type = NotificationType.INVITATION;
        this.message = invitation.getHost().getName() +"님의 " + invitation.getNoteBook().getTitle() + " 공유 초대";
    }

    public NotificationMessageDto(Note note) {
        this.id = note.getId();
        this.type = NotificationType.WRITE_NOTIFICATION;
        this.message = note.getWriter().getName() + "님이 " + note.getNoteBook().getTitle() + "에 새 노트를 작성했습니다.";
    }

    public NotificationMessageDto() {
    }
}
