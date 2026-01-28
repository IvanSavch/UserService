package com.innowise.userservice.exception;


import com.innowise.userservice.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LimitCardException.class)
    public ResponseEntity<ErrorResponse> handleLimitedCardException(LimitCardException limitCardException){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle(limitCardException.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(DuplicateEmailException duplicateEmailException){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle(duplicateEmailException.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(errorResponse);
    }
    @ExceptionHandler(DuplicateCardNumberException.class)
    public ResponseEntity<ErrorResponse> handleNumberAlreadyExists(DuplicateCardNumberException duplicateCardNumberException){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle(duplicateCardNumberException.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(errorResponse);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException userNotFoundException){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle(userNotFoundException.getMessage());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCardNotFoundException(CardNotFoundException cardNotFoundException){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle(cardNotFoundException.getMessage());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidation(MethodArgumentNotValidException m){
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : m.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
