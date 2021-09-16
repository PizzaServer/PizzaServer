package io.github.willqi.pizzaserver.api.level.world.blocks;

import io.github.willqi.pizzaserver.commons.utils.Vector3i;

public enum BlockFace {
    BOTTOM(new Vector3i(0, -1, 0)),
    TOP(new Vector3i(0, 1, 0)),
    NORTH(new Vector3i(0, 0, -1)),
    SOUTH(new Vector3i(0, 0, 1)),
    WEST(new Vector3i(-1, 0, 0)),
    EAST(new Vector3i(1, 0, 0));


    private final Vector3i offset;


    BlockFace(Vector3i offset) {
        this.offset = offset;
    }

    public Vector3i getOffset() {
        return this.offset;
    }

    public static BlockFace resolve(int index) {
        return BlockFace.values()[index % 6];
    }

}
