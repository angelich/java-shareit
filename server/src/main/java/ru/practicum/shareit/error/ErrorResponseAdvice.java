package ru.practicum.shareit.error;

public class ErrorResponseAdvice {
    private final String error;

    public ErrorResponseAdvice(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}