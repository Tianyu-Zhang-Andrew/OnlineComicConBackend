package com.crestruction.web.database.exceptions.AttributeEntityIncompleteException;

public class ExhibitionSpotHasNoIdException extends RuntimeException {
    public ExhibitionSpotHasNoIdException() {
        super ("ExhibitionSpot entity has no id, please add id");
    }
}
