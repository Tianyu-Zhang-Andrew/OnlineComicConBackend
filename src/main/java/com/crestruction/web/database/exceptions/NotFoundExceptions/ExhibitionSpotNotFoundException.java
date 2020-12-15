package com.crestruction.web.database.exceptions.NotFoundExceptions;

public class ExhibitionSpotNotFoundException extends RuntimeException {
    public ExhibitionSpotNotFoundException(Long id) {
        super ("Could not find exhibitionSpot " + id);
    }
}
