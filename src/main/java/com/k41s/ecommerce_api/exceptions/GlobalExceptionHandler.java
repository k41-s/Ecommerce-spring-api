package com.k41s.ecommerce_api.exceptions;

import com.k41s.ecommerce_api.enums.LogLevel;
import com.k41s.ecommerce_api.services.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler(ProductOrderException.class)
    public ResponseEntity<String> handleProductOrderException(ProductOrderException ex) {
        logService.log(LogLevel.ERROR, ex.getMessage());
        if (ex.getMessage().contains("not found")) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND); // 404
        }
        if (ex.getMessage().contains("deleted")) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<ErrorResponse> handleImageProcessingException(ImageProcessingException ex, HttpServletRequest request) {
        // Log the specific exception details
        log.error("Image Processing ERROR: {}", ex.getMessage(), ex);

        ErrorResponse err = new ErrorResponse();
        err.setErrorCode("IMAGE_PROCESSING_FAILED");
        err.setMessage(ex.getMessage());
        err.setStatus(HttpStatus.BAD_REQUEST.value()); // or HttpStatus.INTERNAL_SERVER_ERROR
        err.setTimestamp(Instant.now());
        err.setPath(request.getRequestURI());

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<String> handleUserValidationException(UserValidationException ex) {
        HttpStatus status = ex.getMessage().contains("exists")
                ? HttpStatus.BAD_REQUEST
                : HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(ex.getMessage(), status);
    }

    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity<ErrorResponse> handleJwtExpiredException(JwtExpiredException ex, HttpServletRequest request) {
        logService.log(LogLevel.Warning, "JWT Expired: " + ex.getMessage());

        ErrorResponse err = new ErrorResponse();
        err.setErrorCode("JWT_EXPIRED");
        err.setMessage("Access denied: Token is expired.");
        err.setStatus(HttpStatus.UNAUTHORIZED.value());
        err.setTimestamp(Instant.now());
        err.setPath(request.getRequestURI());

        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtMalformedException.class)
    public ResponseEntity<ErrorResponse> handleJwtMalformedException(JwtMalformedException ex, HttpServletRequest request) {
        logService.log(LogLevel.ERROR, "JWT Malformed: " + ex.getMessage());

        ErrorResponse err = new ErrorResponse();
        err.setErrorCode("JWT_INVALID");
        err.setMessage("Access denied: Invalid or malformed token.");
        err.setStatus(HttpStatus.UNAUTHORIZED.value());
        err.setTimestamp(Instant.now());
        err.setPath(request.getRequestURI());

        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        logService.log(LogLevel.ERROR, ex.getMessage());
        return new ResponseEntity<>("Forbidden: You do not have the required permissions.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        logService.log(LogLevel.Warning, "Failed login attempt: Incorrect password or username.");

        ErrorResponse err = new ErrorResponse();
        err.setErrorCode("BAD_CREDENTIALS");
        err.setMessage("Invalid username or password. Please try again.");
        err.setStatus(HttpStatus.UNAUTHORIZED.value());
        err.setTimestamp(Instant.now());
        err.setPath(request.getRequestURI());

        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
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
