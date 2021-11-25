package io.github.pizzaserver.server.utils;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.api.utils.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

public class ImplLogger implements Logger {

    private final org.apache.logging.log4j.Logger logger;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public ImplLogger(String prefix) {
        this.logger = LogManager.getLogger(prefix);

        if (Server.getInstance().getConfig().isDebugLoggingEnabled()) {
            Configurator.setLevel(this.logger.getName(), Level.DEBUG);
        }
    }

    @Override
    public void info(String message) {
        this.logger.info(message);
    }

    @Override
    public void error(String message) {
        this.error(message, true);
    }

    @Override
    public void error(Throwable exception) {
        this.logger.error(exception);
    }

    @Override
    public void error(String message, Throwable exception) {
        this.error(message, exception, true);
    }

    public void error(String message, boolean colored) {
        if (colored) {
            this.logger.error(ANSI_RED + message + ANSI_RESET);
        } else {
            this.logger.error(message);
        }
    }

    public void error(String message, Throwable exception, boolean colored) {
        if (colored) {
            this.logger.error(ANSI_RED + message + ANSI_RESET, exception);
        } else {
            this.logger.error(message, exception);
        }
    }

    @Override
    public void warn(String message) {
        this.warn(message, true);
    }

    public void warn(String message, boolean colored) {
        if (colored) {
            this.logger.warn(ANSI_YELLOW + message + ANSI_RESET);
        } else {
            this.logger.warn(message);
        }
    }

    @Override
    public void debug(String message) {
        this.logger.debug(message);
    }

    @Override
    public void debug(Throwable exception) {
        this.logger.debug(exception);
    }

    @Override
    public void debug(String message, Throwable exception) {
        this.logger.debug(message, exception);
    }

}
