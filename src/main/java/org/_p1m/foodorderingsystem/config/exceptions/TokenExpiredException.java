package org._p1m.foodorderingsystem.config.exceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) { super(message); }
}
