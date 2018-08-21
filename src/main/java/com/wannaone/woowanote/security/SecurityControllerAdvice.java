package com.wannaone.woowanote.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wannaone.woowanote.exception.UserDuplicatedException;
import com.wannaone.woowanote.validation.ValidationError;
import com.wannaone.woowanote.validation.ValidationErrorsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SecurityControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(SecurityControllerAdvice.class);

    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler(UserDuplicatedException.class)
    public ResponseEntity userDuplicated(UserDuplicatedException exception) throws JsonProcessingException {
        log.debug("UserDuplicatedException is happened!");
        //return new ResponseEntity(exception.getMessage(), HttpStatus.FORBIDDEN);
        ValidationErrorsResponse validationErrorsResponse = new ValidationErrorsResponse();
        validationErrorsResponse.addValidationError(new ValidationError("email", exception.getMessage()));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(validationErrorsResponse);
        //TODO: error Response 위한 DTO 따로 만들기.
    }
}
