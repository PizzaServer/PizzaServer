package io.github.willqi.pizzaserver.api.level.world.blocks;

public enum BlockFace {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static BlockFace resolve(int index) {
        return BlockFace.values()[index % 6];
    }

}
