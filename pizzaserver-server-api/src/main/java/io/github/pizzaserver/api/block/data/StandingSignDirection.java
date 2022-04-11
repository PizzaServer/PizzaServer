package io.github.pizzaserver.api.block.data;

public enum StandingSignDirection {
    SOUTH,
    SOUTH_OF_SOUTHWEST,
    SOUTHWEST,
    WEST_OF_SOUTHWEST,
    WEST,
    WEST_OF_NORTHWEST,
    NORTHWEST,
    NORTH_OF_NORTHWEST,
    NORTH,
    NORTH_OF_NORTHEAST,
    NORTHEAST,
    EAST_OF_NORTHEAST,
    EAST,
    EAST_OF_SOUTHEAST,
    SOUTHEAST,
    SOUTH_OF_SOUTHEAST;

    public static StandingSignDirection fromYaw(float yaw) {
        return StandingSignDirection.values()[(int) Math.floor(((yaw + 180) % 360) / 360d * 16)];
    }
}
