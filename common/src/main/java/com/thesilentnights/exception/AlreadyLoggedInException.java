package com.thesilentnights.exception;

public class AlreadyLoggedInException extends Exception {
    public AlreadyLoggedInException(String username) {
        super(username + ": is already logged in");
    }
}
