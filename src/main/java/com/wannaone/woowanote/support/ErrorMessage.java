package com.wannaone.woowanote.support;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    NOTE_BOOK_NOT_FOUND("NotFound.noteBook"),
    NOTE_NOT_FOUND("NotFound.note"),
    REQUIRE_LOGIN("로그인이 필요합니다."),
    NOTE_BOOK_NOT_BLANK("NotBlank.noteBookDto.title");
    private String messageKey;

    ErrorMessage(String messageKey) {
        this.messageKey = messageKey;
    }
}
