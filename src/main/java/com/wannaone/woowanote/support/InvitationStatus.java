package com.wannaone.woowanote.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvitationStatus {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    private String status;
}
