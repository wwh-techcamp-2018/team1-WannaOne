package com.wannaone.woowanote.exception;

public class InvalidInvitationException extends RuntimeException  {
    public InvalidInvitationException() {
    }

    public InvalidInvitationException(String message) {
        super(message);
    }
}
