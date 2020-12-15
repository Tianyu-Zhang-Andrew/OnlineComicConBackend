package com.crestruction.web.database.exceptions.NotFoundExceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super ("Could not find product " + id);
    }
}
