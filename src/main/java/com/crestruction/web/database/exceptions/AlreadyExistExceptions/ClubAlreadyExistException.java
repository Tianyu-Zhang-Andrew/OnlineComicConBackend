package com.crestruction.web.database.exceptions.AlreadyExistExceptions;

public class ClubAlreadyExistException extends RuntimeException {
    public ClubAlreadyExistException() {
        super ("Club already exist, fail to create club");
    }
}