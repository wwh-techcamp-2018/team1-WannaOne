package com.wannaone.woowanote.support;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    EMAIL_NOT_VALID("Pattern.userDto.email"),
    NOTE_BOOK_NOT_FOUND("NotFound.noteBook"),
    NOTE_NOT_FOUND("NotFound.note"),
    REQUIRE_LOGIN("로그인이 필요합니다."),
    NOTE_BOOK_NOT_BLANK("NotBlank.noteBookDto.title"),
    UNAUTHORIZED("unauthorized.message"),
    FILE_SIZE_LIMIT("최대 업로드 가능한 파일 크기를 초과했습니다."),
    INVITATION_NOT_FOUND("존재하지 않는 초대입니다.");
    private String messageKey;

    ErrorMessage(String messageKey) {
        this.messageKey = messageKey;
    }
}
