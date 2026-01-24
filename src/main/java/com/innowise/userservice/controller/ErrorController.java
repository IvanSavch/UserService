package com.innowise.userservice.controller;

import com.innowise.userservice.exception.CardNotFoundException;
import com.innowise.userservice.exception.LimitCardException;
import com.innowise.userservice.exception.DuplicateEmailException;
import com.innowise.userservice.exception.DuplicateCardNumberException;
import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(LimitCardException.class)
    public ResponseEntity<ErrorResponse> handleLimitedCardException(LimitCardException limitCardException){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle(limitCardException.getMessage());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(DuplicateEmailException duplicateEmailException){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle(duplicateEmailException.getMessage());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    @ExceptionHandler(DuplicateCardNumberException.class)
    public ResponseEntity<ErrorResponse> handleNumberAlreadyExists(DuplicateCardNumberException duplicateCardNumberException){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle(duplicateCardNumberException.getMessage());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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

}
