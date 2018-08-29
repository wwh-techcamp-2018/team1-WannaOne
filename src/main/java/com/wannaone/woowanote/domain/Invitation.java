package com.wannaone.woowanote.domain;

import com.wannaone.woowanote.support.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Invitation extends AuditingDateEntity {

    @ManyToOne
    private User host;

    @ManyToOne
    private User guest;

    @ManyToOne
    private NoteBook noteBook;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status = InvitationStatus.PENDING;

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }
    public Invitation(User host, User guest, NoteBook noteBook) {
        this.host = host;
        this.guest = guest;
        this.noteBook = noteBook;
    }
}
