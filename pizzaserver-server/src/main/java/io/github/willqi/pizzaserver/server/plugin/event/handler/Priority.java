package io.github.willqi.pizzaserver.server.plugin.event.handler;

public enum Priority {

    LOWEST(3), // Event is ran FIRST
    LOWER(2),
    LOW(1),
    NORMAL(0),
    HIGH(-1),
    HIGHER(-2),
    HIGHEST(-3); // Event is ran LAST, thus the ID should put it at the end of the line.

    private int value;

    Priority(int value) { this.value = value; }
    public int getValue() { return value; }
}
