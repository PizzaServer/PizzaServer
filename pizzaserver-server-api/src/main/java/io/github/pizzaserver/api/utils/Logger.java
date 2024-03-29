package io.github.pizzaserver.api.utils;

/**
 * Utility interface for logging messages.
 */
public interface Logger {

    void info(String message);

    void error(String message);

    void error(Throwable exception);

    void error(String message, Throwable exception);

    void warn(String message);

    void debug(String message);

    void debug(Throwable exception);

    void debug(String message, Throwable exception);

}
