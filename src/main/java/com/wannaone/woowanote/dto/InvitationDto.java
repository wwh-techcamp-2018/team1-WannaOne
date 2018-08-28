package com.wannaone.woowanote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationDto {

    private List<Long> guestIdList;
    private Long notebookId;
}
