package com.crestruction.web.database.exceptions.AlreadyExistExceptions;

public class ClubProductAlreadyExistException extends RuntimeException {
    public ClubProductAlreadyExistException() {
        super ("ClubProduct already exist, fail to create clubProduct");
    }
}
