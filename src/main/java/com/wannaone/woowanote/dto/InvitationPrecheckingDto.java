package com.wannaone.woowanote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationPrecheckingDto {

    private String guestEmail;
    private Long notebookId;
}
