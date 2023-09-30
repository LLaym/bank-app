package ru.astondevs.bankapp.util.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.astondevs.bankapp.util.exception.NoAccessException;
import ru.astondevs.bankapp.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static ru.astondevs.bankapp.util.Constants.FORMATTER;

@Slf4j
@RestControllerAdvice("ru.astondevs.bankapp")
public class ErrorHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleBadRequestError(final Throwable e) {
        String errorStatus = "BAD_REQUEST";
        String errorReason = "Incorrectly made request.";
        String errorMessage = e.getMessage();
        String errorTimestamp = LocalDateTime.now().format(FORMATTER);

        log.warn("{}. {}", errorReason, errorMessage);
        return new Error(errorStatus, errorReason, errorMessage, errorTimestamp);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundError(final Throwable e) {
        String errorStatus = "NOT_FOUND";
        String errorReason = "The required object was not found.";
        String errorMessage = e.getMessage();
        String errorTimestamp = LocalDateTime.now().format(FORMATTER);

        log.warn("{}. {}", errorReason, errorMessage);
        return new Error(errorStatus, errorReason, errorMessage, errorTimestamp);
    }

    @ExceptionHandler({NoAccessException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleConditionError(final Throwable e) {
        String errorStatus = "FORBIDDEN";
        String errorReason = "For the requested operation the conditions are not met.";
        String errorMessage = e.getMessage();
        String errorTimestamp = LocalDateTime.now().format(FORMATTER);

        log.warn("{}. {}", errorReason, errorMessage);
        return new Error(errorStatus, errorReason, errorMessage, errorTimestamp);
    }
}