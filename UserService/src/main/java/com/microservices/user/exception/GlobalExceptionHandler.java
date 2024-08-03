package com.microservices.user.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UsernameNotFoundException usernameNotFoundException){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "101",
                usernameNotFoundException.getMessage(),
                "User not registered with us !! Please try with registered username or register first");
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialException(BadCredentialsException badCredentialsException){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "102",
                badCredentialsException.getMessage(),
                "Please enter correct username/password");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialException(DataIntegrityViolationException dataIntegrityViolationException){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "301",
                dataIntegrityViolationException.getMessage(),
                "User with similar username/email already exists");
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialException(NullPointerException runtimeException){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "1003",
                runtimeException.getMessage(),
                "Something went too wrong. Please try again after sometime");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialException(RuntimeException runtimeException){
        System.out.println("Runtime exception message : " + runtimeException.getMessage());
        System.out.println("Runtime exception cause : " + runtimeException.getCause());
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "5000",
                runtimeException.getMessage(),
                "Something went too wrong. Please try again after sometime");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
