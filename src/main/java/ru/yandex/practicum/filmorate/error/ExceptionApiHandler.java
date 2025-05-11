package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NoContentException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionApiHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse customValidation(ValidationException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponse(exception.getMessage(), "Validation error");
    }

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse entityIsAlreadyExist(AlreadyExistException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponse(exception.getMessage(), "Entity is already exist!");

    }

    @ExceptionHandler(NoContentException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ErrorResponse entityIsNotFound(NoContentException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponse(exception.getMessage(), "Entity is not found!");

    }

    @ExceptionHandler(NotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityIsNotExist(NotExistException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponse(exception.getMessage(), "Entity is not found!");

    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequest(ru.yandex.practicum.filmorate.exception.BadRequestException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponse(exception.getMessage(), "Fields validation exception");

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void commonValidation(MethodArgumentNotValidException e) {
        List<FieldError> items = e.getBindingResult().getFieldErrors();
        String message = items.stream()
                .map(FieldError::getField)
                .findFirst().get() + " - "
                + items.stream()
                .map(FieldError::getDefaultMessage)
                .findFirst().get();
        log.warn(message);
        throw new ru.yandex.practicum.filmorate.exception.BadRequestException("Validation exception");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherExceptions(final Throwable e) {
        log.warn(e.getMessage());

        return new ErrorResponse(e.getMessage(), "Unknown error");
    }
}
