package com.wannaone.woowanote.dto;

import com.wannaone.woowanote.domain.Invitation;
import com.wannaone.woowanote.support.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationAnswerDto {
    private InvitationStatus response;
    private Long invitationId;
}
