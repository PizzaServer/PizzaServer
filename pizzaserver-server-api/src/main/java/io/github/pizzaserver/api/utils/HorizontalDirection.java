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

    /**
     * Some block states have an up and down direction. We need to take that offset into consideration.
     * @return block state index of omni directional block
     */
    public int getOmniBlockStateIndex() {
        return this.ordinal() + 2;
    }

    /**
     * Get the block state for a block without an up/down state.
     * @return block state
     */
    public int getBlockStateIndex() {
        return this.ordinal();
    }

    /**
     * Some block states have an up and down direction. We need to take that offset into consideration.
     * @param index omni block state
     * @return direction representative from the omni block state index
     */
    public static HorizontalDirection fromOmniBlockStateIndex(int index) {
        return HorizontalDirection.values()[index - 2];
    }

    /**
     * Get the direction for a block without an up/down state.
     * @param index block state
     * @return direction representative from the block state index
     */
    public static HorizontalDirection fromBlockStateIndex(int index) {
        return HorizontalDirection.values()[index];
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
