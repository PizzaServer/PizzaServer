package io.github.willqi.pizzaserver.server.utils;

import io.github.willqi.pizzaserver.api.utils.Logger;
import org.apache.logging.log4j.LogManager;

public class BedrockLogger implements Logger {

    private final org.apache.logging.log4j.Logger logger;

    public BedrockLogger(String prefix) {
        this.logger = LogManager.getLogger(prefix);
    }

    @Override
    public void info(String message) {
        this.logger.info(message);
    }

    @Override
    public void error(String message) {
        this.logger.error(message);
    }

    @Override
    public void error(Throwable exception) {
        this.logger.error(exception);
    }

    @Override
    public void error(String message, Throwable exception) {
        this.logger.error(message, exception);
    }

    @Override
    public void warn(String message) {
        this.logger.warn(message);
    }

    @Override
    public void debug(String message) {
        this.logger.debug(message);
    }

}
