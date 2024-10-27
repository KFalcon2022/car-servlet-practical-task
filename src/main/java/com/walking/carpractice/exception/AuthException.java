package com.walking.carpractice.exception;

public class AuthException extends CommonAppException {
    public AuthException() {
        super("Неверный логин или пароль");
    }
}
