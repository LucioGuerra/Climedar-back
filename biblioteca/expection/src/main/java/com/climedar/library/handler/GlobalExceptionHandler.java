package com.climedar.library.handler;

import com.climedar.library.dto.ApiError;
import com.climedar.library.exception.ClimedarException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClimedarException.class)
    private ResponseEntity<ApiError> handlerCriminalCrossException(ClimedarException e){
        log.error(String.valueOf(e));

        ApiError apiError = new ApiError(e.getCode(), e.getMessage(), BAD_REQUEST.value());

        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<ApiError> handlerEntityNotFoundException(EntityNotFoundException e) {
        log.error(String.valueOf(e));

        ApiError apiError = new ApiError("ENTITY_NOT_FOUND", e.getMessage(), HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ApiError> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(String.valueOf(e));

        ApiError apiError = new ApiError("VALIDATION_ERROR", e.getMessage(), BAD_REQUEST.value());

        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ApiError> handlerHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(String.valueOf(e));

        ApiError apiError = new ApiError("MALFORMED_JSON_REQUEST", e.getMessage(), BAD_REQUEST.value());

        return ResponseEntity.status(BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ApiError> handlerException(Exception e) {
        log.error(String.valueOf(e));

        ApiError apiError = new ApiError("INTERNAL_SERVER_ERROR", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<ApiError> handlerDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(String.valueOf(e));

        ApiError apiError = new ApiError("DATA_INTEGRITY_VIOLATION", e.getMessage(), BAD_REQUEST.value());

        return ResponseEntity.status(BAD_REQUEST).body(apiError);
    }
}
