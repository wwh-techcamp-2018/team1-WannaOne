package com.wannaone.woowanote.security;

import com.wannaone.woowanote.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@ControllerAdvice
public class SecurityControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(SecurityControllerAdvice.class);

    @ExceptionHandler(UserDuplicatedException.class)
    public ResponseEntity userDuplicated(UserDuplicatedException exception) {
        log.debug("UserDuplicatedException is happened!");
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
    }

    @ExceptionHandler(UnAuthenticationException.class)
    public ResponseEntity unAuthentication(UnAuthenticationException exception) {
        log.debug("UnAuthenticationException is happened!");
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity unAuthentication(UnAuthorizedException exception) {
        log.debug("UnAuthorizedException is happened!");
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
    }

    @ExceptionHandler(AlreadyIncludeSharedNoteBookException.class)
    public ResponseEntity alreadyIncludeShardNoteBook(AlreadyIncludeSharedNoteBookException exception) {
        log.debug("AlreadyIncludeSharedNoteBookException is happened!");
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
    }

    @ExceptionHandler(AlreadyInviteGuestExcetion.class)
    public ResponseEntity alreadyInviteGuest(AlreadyInviteGuestExcetion exception) {
        log.debug("AlreadyInviteGuestExcetion is happened!");
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
    }
}
