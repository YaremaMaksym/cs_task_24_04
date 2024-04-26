package yaremax.com.cs_task_24_04.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Builder
public record ApiException (
        String message,
        HttpStatus httpStatus,
        ZonedDateTime timeStamp) {
}
