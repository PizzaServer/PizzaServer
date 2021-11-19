package io.github.pizzaserver.api.level.world.blocks;

import com.nukkitx.math.vector.Vector3i;

public enum BlockFace {
    BOTTOM(Vector3i.from(0, -1, 0)),
    TOP(Vector3i.from(0, 1, 0)),
    NORTH(Vector3i.from(0, 0, -1)),
    SOUTH(Vector3i.from(0, 0, 1)),
    WEST(Vector3i.from(-1, 0, 0)),
    EAST(Vector3i.from(1, 0, 0));


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
