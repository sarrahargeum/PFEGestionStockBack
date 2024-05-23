package com.example.stock.exception;

import lombok.Getter;

import java.util.List;

public class InvalidEntityException extends RuntimeException {


    @Getter
    private List<String> errors;

    public InvalidEntityException(String message) {
        super(message);
    }

    public InvalidEntityException(String message, Throwable cause) {
        super(message, cause);
    }




    public InvalidEntityException(String message, List<String> errors) {
        super(message);

        this.errors = errors;
    }



}