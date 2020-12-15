package com.crestruction.web.database.exceptions.AlreadyExistExceptions;

public class ExhibitionSpotAlreadyExistException extends RuntimeException {
    public ExhibitionSpotAlreadyExistException() {
        super ("ExhibitionSpot already exist, fail to create club");
    }
}
