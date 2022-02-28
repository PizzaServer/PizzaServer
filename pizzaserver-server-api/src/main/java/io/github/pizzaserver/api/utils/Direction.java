package io.github.pizzaserver.api.utils;

import io.github.pizzaserver.api.block.data.BlockFace;

public enum Direction {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public boolean isVertical() {
        return this == DOWN || this == UP;
    }

    public boolean isHorizontal() {
        return !this.isVertical();
    }

    public Direction opposite() {
        return switch (this) {
            case DOWN -> UP;
            case UP -> DOWN;
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case EAST -> WEST;
        };
    }

    public HorizontalDirection toHorizontal() {
        return switch (this) {
            case UP, DOWN -> null;
            case NORTH -> HorizontalDirection.NORTH;
            case SOUTH -> HorizontalDirection.SOUTH;
            case EAST -> HorizontalDirection.EAST;
            case WEST -> HorizontalDirection.WEST;
        };
    }

    public BlockFace toBlockFace() {
        return switch (this) {
            case UP -> BlockFace.TOP;
            case DOWN -> BlockFace.BOTTOM;
            case NORTH -> BlockFace.NORTH;
            case SOUTH -> BlockFace.SOUTH;
            case WEST -> BlockFace.WEST;
            case EAST -> BlockFace.EAST;
        };
    }

    public int getBlockStateIndex() {
        return this.ordinal();
    }

    public static Direction fromBlockStateIndex(int index) {
        return Direction.values()[index];
    }

    public static Direction fromBlockFace(BlockFace face) {
        return switch (face) {
            case TOP -> UP;
            case BOTTOM -> DOWN;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
        };
    }

    public static Direction fromHorizontal(HorizontalDirection direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
        };
    }

}
