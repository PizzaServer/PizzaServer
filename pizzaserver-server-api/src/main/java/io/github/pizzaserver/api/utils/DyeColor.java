package io.github.pizzaserver.api.utils;

public enum DyeColor {
    WHITE("white", "White"),
    ORANGE("orange", "Orange"),
    MAGENTA("magenta", "Magenta"),
    LIGHT_BLUE("light_blue", "Light Blue"),
    YELLOW("yellow", "Yellow"),
    LIME("lime", "Lime"),
    PINK("pink", "Pink"),
    GRAY("gray", "Gray"),
    LIGHT_GRAY("silver", "Light Gray"),
    CYAN("cyan", "Cyan"),
    PURPLE("purple", "Purple"),
    BLUE("blue", "Blue"),
    BROWN("brown", "Brown"),
    GREEN("green", "Green"),
    RED("red", "Red"),
    BLACK("black", "Black");

    private final String id;
    private final String displayName;

    DyeColor(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
