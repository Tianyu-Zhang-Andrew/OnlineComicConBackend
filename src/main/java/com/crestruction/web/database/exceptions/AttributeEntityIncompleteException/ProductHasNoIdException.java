package com.crestruction.web.database.exceptions.AttributeEntityIncompleteException;

public class ProductHasNoIdException extends RuntimeException {
    public ProductHasNoIdException() {
        super ("product entity has no id, please add id");
    }
}
