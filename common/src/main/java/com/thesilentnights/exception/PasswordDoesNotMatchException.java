package com.thesilentnights.exception;

public class PasswordDoesNotMatchException extends Exception {
    public String password1;
    public String repeat;

    public PasswordDoesNotMatchException(String password1, String repeat) {
        this.password1 = password1;
        this.repeat = repeat;
    }
}
