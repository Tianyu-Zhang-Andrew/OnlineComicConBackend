package com.crestruction.web.database.exceptions.AlreadyExistExceptions;

public class ProductSpotAlreadyExistException extends RuntimeException {
    public ProductSpotAlreadyExistException() {
        super ("ProductSpot already exist, fail to create clubProduct");
    }
}
