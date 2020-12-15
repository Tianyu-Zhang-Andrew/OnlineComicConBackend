package com.crestruction.web.database.exceptions.NotFoundExceptions;

public class ClubProductNotFoundException extends RuntimeException {
    public ClubProductNotFoundException() {
        super ("Could not find clubProduct");
    }
}
