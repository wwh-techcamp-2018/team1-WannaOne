package com.wannaone.woowanote.exception;

import com.wannaone.woowanote.dto.UserNameDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class AlreadyInviteGuestExcetion extends RuntimeException  {
    private String message;

    public AlreadyInviteGuestExcetion(List<UserNameDto> userNameDtos) {
        this.message = userNameDtos.stream().map((dto) -> dto.getName()).collect(Collectors.joining(", "));
        this.message += "님은 이 노트북에 초대 중 입니다.";
    }

    public AlreadyInviteGuestExcetion(String message) {
        super(message);
    }
}
