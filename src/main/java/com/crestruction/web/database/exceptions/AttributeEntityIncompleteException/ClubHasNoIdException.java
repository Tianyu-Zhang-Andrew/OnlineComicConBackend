package com.crestruction.web.database.exceptions.AttributeEntityIncompleteException;

public class ClubHasNoIdException extends RuntimeException {
    public ClubHasNoIdException() {
        super ("Club entity has no id, please add id");
    }
}
