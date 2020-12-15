package com.crestruction.web.database.exceptions.NotFoundExceptions;

public class TableIsEmptyException extends RuntimeException {
    public TableIsEmptyException() {
        super ("There is no record in the database now, database is empty.");
    }
}
