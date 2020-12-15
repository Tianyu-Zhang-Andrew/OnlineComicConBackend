package com.crestruction.web.database.exceptions.AlreadyExistExceptions;

public class ProductAlreadyExistException extends RuntimeException {
    public ProductAlreadyExistException() {
        super ("Product already exist, fail to create product");
    }
}
