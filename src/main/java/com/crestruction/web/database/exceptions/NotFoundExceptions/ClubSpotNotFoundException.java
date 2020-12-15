package com.crestruction.web.database.exceptions.NotFoundExceptions;

public class ClubSpotNotFoundException extends RuntimeException {
    public ClubSpotNotFoundException() {
        super ("Could not find clubSpot");
    }
}
