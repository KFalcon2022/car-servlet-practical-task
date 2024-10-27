package com.walking.carpractice.exception;

public class AuthException extends RuntimeException {
    public AuthException() {
        super("Неверный логин или пароль");
    }
}
