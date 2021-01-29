package ru.zhenyria.restaurants.util.exception;

public class NotAvailableOperationException extends RuntimeException {
    public NotAvailableOperationException(String message) {
        super(message);
    }
}
