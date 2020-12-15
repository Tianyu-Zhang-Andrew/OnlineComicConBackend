package com.crestruction.web.database.exceptions.NotFoundExceptions;

public class ProductSpotNotFoundException extends RuntimeException {
    public ProductSpotNotFoundException() {
        super ("Could not find productSpot");
    }
}
