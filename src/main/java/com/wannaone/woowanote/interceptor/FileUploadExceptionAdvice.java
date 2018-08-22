//package com.wannaone.woowanote.interceptor;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.multipart.MaxUploadSizeExceededException;
//
//@ControllerAdvice
//public class FileUploadExceptionAdvice {
//
//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public ResponseEntity handleMaxSizeException(MaxUploadSizeExceededException exception) {
//        return new ResponseEntity(exception.getMessage(), HttpStatus.FORBIDDEN);
//    }
//}
