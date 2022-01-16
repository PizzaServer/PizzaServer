package io.github.pizzaserver.api.scoreboard;

/**
 * Location of where a scoreboard should be displayed.
 */
public enum DisplaySlot {
    LIST("list"),
    SIDEBAR("sidebar"),
    BELOW_NAME("belowname");

    private final String id;


    DisplaySlot(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
