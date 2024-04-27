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

    @ExceptionHandler(value = InvalidDataException.class)
    public ResponseEntity<Object> handleInvalidDataException(RuntimeException ex, HttpServletRequest request){
        ApiException apiException = ApiException.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .timeStamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();

        LOGGER.error("⚠⚠⚠ Exception was thrown with message: {}", ex.getMessage());

        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(value = DuplicateResourceException.class)
    public ResponseEntity<Object> handleDuplicateResourceException(RuntimeException ex, HttpServletRequest request){
        ApiException apiException = ApiException.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .message(ex.getMessage())
                .timeStamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();

        LOGGER.error("⚠⚠⚠ Exception was thrown with message: {}", ex.getMessage());

        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(RuntimeException ex, HttpServletRequest request){
        ApiException apiException = ApiException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .timeStamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();

        LOGGER.error("⚠⚠⚠ Exception was thrown with message: {}", ex.getMessage());

        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }
}
