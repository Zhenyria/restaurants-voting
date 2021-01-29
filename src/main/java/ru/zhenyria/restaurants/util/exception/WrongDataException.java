package ru.zhenyria.restaurants.util.exception;

public class WrongDataException extends RuntimeException {
    public WrongDataException(String message) {
        super(message);
    }
}
