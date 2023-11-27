package ru.yandex.practicum.filmorate.exception;

public class ParamNotExistException extends RuntimeException {
    public ParamNotExistException(String message) {
        super(message);
    }
}
