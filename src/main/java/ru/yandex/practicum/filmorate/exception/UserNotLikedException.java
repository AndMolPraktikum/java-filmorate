package ru.yandex.practicum.filmorate.exception;

public class UserNotLikedException extends RuntimeException {

    public UserNotLikedException() {
        super();
    }

    public UserNotLikedException(String message) {
        super(message);
    }

    public UserNotLikedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotLikedException(Throwable cause) {
        super(cause);
    }

    protected UserNotLikedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
