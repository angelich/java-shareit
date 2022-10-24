package ru.practicum.shareit.error;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ErrorHandlerAdvice {

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponseAdvice handleValidationException(IllegalArgumentException e) {
        return new ErrorResponseAdvice(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorResponseAdvice handleNoSuchElementException(NoSuchElementException e) {
        return new ErrorResponseAdvice(e.getMessage());
    }
}
