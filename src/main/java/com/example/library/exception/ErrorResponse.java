package com.example.library.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Standard error response returned by the GlobalExceptionHandler.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    private int status;

    private String error;

    private String message;

    public ErrorResponse(HttpStatus httpStatus, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
    }
}
