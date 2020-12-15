package com.crestruction.web.database.exceptions.NotFoundExceptions;

public class NoResultFoundException extends RuntimeException {
    public NoResultFoundException() {
        super ("No result is found");
    }
}
