package yaremax.com.cs_task_24_04.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String LOGGER_MESSAGE_PREFIX = "⚠⚠⚠ Exception was thrown with message: ";

    private ApiException buildApiException(RuntimeException ex, HttpStatus httpStatus) {
        return ApiException.builder()
                .httpStatus(httpStatus)
                .message("(" + ex.getClass().getSimpleName() + ") " + ex.getMessage())
                .timeStamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();
    }

    private ResponseEntity<Object> handleException(RuntimeException ex, HttpServletRequest request, HttpStatus httpStatus) {
        ApiException apiException = buildApiException(ex, httpStatus);
        LOGGER.error(LOGGER_MESSAGE_PREFIX + "{}", ex.getMessage());
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }


    @ExceptionHandler(value = {InvalidDataException.class})
    public ResponseEntity<Object> handleBadRequestExceptions(RuntimeException ex, HttpServletRequest request){
        return handleException(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {DuplicateResourceException.class})
    public ResponseEntity<Object> handleConflictExceptions(RuntimeException ex, HttpServletRequest request){
        return handleException(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundExceptions(RuntimeException ex, HttpServletRequest request){
        return handleException(ex, request, HttpStatus.NOT_FOUND);
    }
}
