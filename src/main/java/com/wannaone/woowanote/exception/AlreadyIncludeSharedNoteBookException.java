package com.wannaone.woowanote.exception;

import com.wannaone.woowanote.dto.UserNameDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class AlreadyIncludeSharedNoteBookException extends RuntimeException  {
    private String message;

    public AlreadyIncludeSharedNoteBookException(List<UserNameDto> userNameDtos) {
        this.message = userNameDtos.stream().map((dto) -> dto.getName()).collect(Collectors.joining(", "));
        this.message += "님은 이미 이 노트북을 공유하고 있습니다.";
    }

    public AlreadyIncludeSharedNoteBookException(String message) {
        super(message);
    }
}
