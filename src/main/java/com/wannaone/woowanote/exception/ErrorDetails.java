package com.wannaone.woowanote.exception;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorDetails {
    private Date timestamp;
    private String message;
}
