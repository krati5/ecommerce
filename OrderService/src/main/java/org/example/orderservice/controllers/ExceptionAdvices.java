package org.example.orderservice.controllers;

import org.example.orderservice.exceptions.AuthenticationException;
import org.example.orderservice.exceptions.ErrorResponseDto;
import org.example.orderservice.exceptions.InsufficientQuantity;
import org.example.orderservice.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvices {

    @ExceptionHandler({NotFoundException.class, InsufficientQuantity.class})
    public ResponseEntity<ErrorResponseDto> exception(Exception exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(exception.getMessage());
        return new ResponseEntity(errorResponseDto, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("An unexpected error occurred: " + exception.getMessage());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(Exception exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(exception.getMessage());
        return new ResponseEntity(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }
}
