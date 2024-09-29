package org.example.orderservice.exceptions;

public class NotFoundException extends Exception{
    public NotFoundException(String message){
        super(message);
    }
}
