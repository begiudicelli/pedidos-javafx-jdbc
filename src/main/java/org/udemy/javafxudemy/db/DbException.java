package org.udemy.javafxudemy.db;

public class DbException extends RuntimeException {

    public DbException(String msg) {
        super(msg);
    }

    public DbException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
