package com.wannaone.woowanote.support;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    NOTE_BOOK_NOT_FOUND("NotFound.noteBook"),
    NOTE_NOT_FOUND("NotFound.note");
    private String messageKey;

    ErrorMessage(String messageKey) {
        this.messageKey = messageKey;
    }
}
