package org.example.orderservice.exceptions;

public class ResponseStatusException extends Exception{
    public ResponseStatusException(String message){
        super(message);
    }
}
