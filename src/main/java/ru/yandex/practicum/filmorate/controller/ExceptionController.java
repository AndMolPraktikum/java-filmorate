package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleUserValidationException(final UserValidationException e) {
        return new Response(
                String.format("%s %s", LocalDateTime.now(), e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleFilmValidationException(final FilmValidationException e) {
        return new Response(
                String.format("%s %s", LocalDateTime.now(), e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleFilmNotFoundException(final FilmNotFoundException e) {
        return new Response(
                String.format("%s %s", LocalDateTime.now(), e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleUserNotFoundException(final UserNotFoundException e) {
        return new Response(
                String.format("%s %s", LocalDateTime.now(), e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response handleUserAlreadyExistException(final UserAlreadyExistException e) {
        return new Response(
                String.format("%s %s", LocalDateTime.now(), e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleUserNotLikedException(final UserNotLikedException e) {
        return new Response(
                String.format("%s %s", LocalDateTime.now(), e.getMessage())
        );
    }
}
