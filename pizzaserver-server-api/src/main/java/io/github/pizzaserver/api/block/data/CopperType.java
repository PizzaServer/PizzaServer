package io.github.pizzaserver.api.block.data;

public enum CopperType {
    NORMAL(null),
    EXPOSED("Exposed"),
    WEATHERED("Weathered"),
    OXIDIZED("Oxidized");

    private final String displayName;


    CopperType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
