package org.example.orderservice.exceptions;

public class InsufficientQuantity extends Exception{
    public InsufficientQuantity(String message){
        super(message);
    }

}

