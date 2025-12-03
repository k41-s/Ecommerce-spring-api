package com.k41s.ecommerce_api.exceptions;

import com.k41s.ecommerce_api.enums.LogLevel;
import com.k41s.ecommerce_api.services.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LogService logService;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        logService.log(LogLevel.ERROR, ex.getMessage());

        ErrorResponse err = new ErrorResponse();
        err.setErrorCode(ex.getErrorCode());
        err.setMessage(ex.getMessage());
        err.setStatus(ex.getStatus().value());
        err.setTimestamp(Instant.now());
        err.setPath(request.getRequestURI());

        return new ResponseEntity<>(err, ex.getStatus());
    }

    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<ErrorResponse> handleImageProcessingException(ImageProcessingException ex, HttpServletRequest request) {
        // Log the specific exception details
        log.error("Image Processing Error: {}", ex.getMessage(), ex);

        ErrorResponse err = new ErrorResponse();
        err.setErrorCode("IMAGE_PROCESSING_FAILED");
        err.setMessage(ex.getMessage());
        err.setStatus(HttpStatus.BAD_REQUEST.value()); // or HttpStatus.INTERNAL_SERVER_ERROR
        err.setTimestamp(Instant.now());
        err.setPath(request.getRequestURI());

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        logService.log(LogLevel.ERROR, ex.getMessage());

        ErrorResponse err = new ErrorResponse();
        err.setErrorCode("INTERNAL_SERVER_ERROR");
        err.setMessage("An unexpected error occurred");
        err.setStatus(500);
        err.setTimestamp(Instant.now());
        err.setPath(request.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
