package ru.yandex.practicum.filmorate.exception;

public class FilmValidationException extends RuntimeException {

    public FilmValidationException() {
        super();
    }

    public FilmValidationException(String message) {
        super(message);
    }

    public FilmValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilmValidationException(Throwable cause) {
        super(cause);
    }

    protected FilmValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
