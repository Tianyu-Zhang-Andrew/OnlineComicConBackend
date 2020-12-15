package com.crestruction.web.database.exceptions.NotFoundExceptions;

public class ClubNotFoundException extends RuntimeException {
    public ClubNotFoundException(Long id) {
        super ("Could not find club " + id);
    }
}