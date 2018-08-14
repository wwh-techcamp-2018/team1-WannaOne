package com.wannaone.woowanote.exception;

public class UserDuplicatedException extends RuntimeException {
    public UserDuplicatedException() {
        super();
    }

    public UserDuplicatedException(String message) {
        super(message);
    }
}
