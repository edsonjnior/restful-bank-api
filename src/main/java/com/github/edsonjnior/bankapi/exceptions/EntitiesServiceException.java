package com.github.edsonjnior.bankapi.exceptions;

import java.util.function.Supplier;

public class EntitiesServiceException extends RuntimeException{
    public EntitiesServiceException(String message) {
        super(message);
    }

    public EntitiesServiceException() {
        super();
    }
}
