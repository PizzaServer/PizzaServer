package io.github.willqi.pizzaserver.commons.exception;

public class MissingPropertyException extends RuntimeException {

    public MissingPropertyException(String description) {
        super(description);
    }

}
