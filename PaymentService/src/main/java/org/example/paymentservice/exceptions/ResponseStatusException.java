package org.example.paymentservice.exceptions;

public class ResponseStatusException extends Exception{
    public ResponseStatusException(String message){
        super(message);
    }
}
