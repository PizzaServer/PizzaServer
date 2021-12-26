package io.github.pizzaserver.api.utils;

public enum HorizontalDirection {
    NORTH,
    SOUTH,
    EAST,
    WEST;


    public HorizontalDirection opposite() {
        switch (this) {
            case EAST:
                return WEST;
            case WEST:
                return EAST;
            case SOUTH:
                return NORTH;
            case NORTH:
            default:
                return SOUTH;
        }
    }

    public static HorizontalDirection fromYaw(float yaw) {
        return null;
    }
}
