package io.github.willqi.pizzaserver.server.utils;

import org.apache.logging.log4j.LogManager;

public class Logger {

    private final org.apache.logging.log4j.Logger logger;

    public Logger(String prefix) {
        this.logger = LogManager.getLogger(prefix);
    }

    public void info(String message) {
        this.logger.info(message);
    }

    public void error(String message) {
        this.logger.error(message);
    }

    public void error(Throwable exception) {
        this.logger.error(exception);
    }

    public void error(String message, Throwable exception) {
        this.logger.error(message, exception);
    }

    public void warn(String message) {
        this.logger.warn(message);
    }

    public void debug(String message) {
        this.logger.debug(message);
    }

}
