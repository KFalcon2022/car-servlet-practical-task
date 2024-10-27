package com.walking.carpractice.exception;

public class DuplicateUserException extends CommonAppException {
    public DuplicateUserException() {
        super("Пользователь с таким именем уже существует");
    }
}
