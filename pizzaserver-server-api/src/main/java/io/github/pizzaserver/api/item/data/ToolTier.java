package io.github.pizzaserver.api.item.data;

public enum ToolTier {
    NONE(1),
    WOOD(2),
    STONE(4),
    IRON(6),
    GOLD(12),
    DIAMOND(8),
    NETHERITE(9);


    private final int strength;

    ToolTier(int strength) {
        this.strength = strength;
    }

    public int getStrength() {
        return this.strength;
    }

}
