package com.wannaone.woowanote.exception;

import com.wannaone.woowanote.support.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class MultipartFileControllerAdvice {
    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    public ResponseEntity handleFileException(HttpServletRequest request, Throwable ex) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ErrorMessage.FILE_SIZE_LIMIT.getMessageKey());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }
}
