package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleValidationException(IllegalArgumentException e) {
        log.warn("Bad request, message={}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        log.warn("Bad request, message={}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleNoSuchElementException(NoSuchElementException e) {
        log.warn("Not found, message={}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllExceptions(Throwable e) {
        log.error("Internal error, message={}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
