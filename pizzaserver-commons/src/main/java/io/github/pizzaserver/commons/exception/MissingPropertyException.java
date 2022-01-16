package io.github.pizzaserver.commons.exception;

public class MissingPropertyException extends RuntimeException {

    public MissingPropertyException(String description) {
        super(description);
    }
}
