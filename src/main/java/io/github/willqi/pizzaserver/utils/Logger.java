package io.github.willqi.pizzaserver.utils;

public class Logger {

    private static final String RED_CONSOLE_CHAR = "\033[0;31m";
    private static final String YELLOW_CONSOLE_CHAR = "\033[0;33m";

    private final String prefix;

    public Logger(String prefix) {
        this.prefix = prefix;
    }

    public void info(String message) {
        System.out.printf("[%s] %s%n\n", prefix, message);
    }

    public void error(String message) {
        System.out.printf("%s[%s] %s%n\n", RED_CONSOLE_CHAR, prefix, message);
    }

    public void error(Exception exception) {
        this.error(exception.toString());
    }

    public void warn(String message) {
        System.out.printf("%s[%s] %s%n\n", YELLOW_CONSOLE_CHAR, prefix, message);
    }

}
