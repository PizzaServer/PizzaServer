package io.github.pizzaserver.api.utils;

public enum HorizontalDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST;


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

    public int getBlockStateIndex() {
        return this.ordinal() + 2;
    }

    public static HorizontalDirection fromYaw(float yaw) {
        float angle = yaw % 360;
        if (angle < 0) {
            angle += 360;
        }

        // Thanks PocketMine
        if ((0 <= angle && angle < 45) || (315 <= angle && angle < 360)) {
            return SOUTH;
        } else if (45 <= angle && angle < 135) {
            return WEST;
        } else if (135 <= angle && angle < 225) {
            return NORTH;
        } else {
            return EAST;
        }
    }
}
