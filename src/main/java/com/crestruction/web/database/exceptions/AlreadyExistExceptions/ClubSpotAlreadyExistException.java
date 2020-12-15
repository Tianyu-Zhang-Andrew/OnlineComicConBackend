package com.crestruction.web.database.exceptions.AlreadyExistExceptions;

public class ClubSpotAlreadyExistException extends RuntimeException {
    public ClubSpotAlreadyExistException() {
        super ("ClubSpot already exist, fail to create ClubSpot");
    }
}
