package io.github.willqi.pizzaserver.server.player.playerdata;

import io.github.willqi.pizzaserver.api.player.data.Gamemode;
import io.github.willqi.pizzaserver.commons.utils.Check;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.api.level.world.data.Dimension;

public class PlayerData {

    private final String levelName;
    private final Dimension dimension;
    private final Gamemode gamemode;
    private final Vector3 position;
    private final float pitch;
    private final float yaw;


    private PlayerData(String levelName,
                       Dimension dimension,
                       Gamemode gamemode,
                       Vector3 position,
                       float pitch,
                       float yaw) {
        this.levelName = levelName;
        this.dimension = dimension;
        this.gamemode = gamemode;
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    /**
     * Retrieve the name of the {@link io.github.willqi.pizzaserver.api.level.Level} the player is in.
     * @return world name
     */
    public String getLevelName() {
        return this.levelName;
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public Gamemode getGamemode() {
        return this.gamemode;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }


    public static class Builder {

        private String levelName;
        private Dimension dimension;
        private Gamemode gamemode;
        private Vector3 position;
        private float pitch;
        private float yaw;


        public Builder setLevelName(String levelName) {
            this.levelName = levelName;
            return this;
        }

        public Builder setDimension(Dimension dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder setGamemode(Gamemode gamemode) {
            this.gamemode = gamemode;
            return this;
        }

        public Builder setPosition(Vector3 position) {
            this.position = position;
            return this;
        }

        public Builder setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public Builder setYaw(float yaw) {
            this.yaw = yaw;
            return this;
        }

        public PlayerData build() {
            return new PlayerData(
                    Check.nullParam(this.levelName, "levelName"),
                    Check.nullParam(this.dimension, "dimension"),
                    Check.nullParam(this.gamemode, "gamemode"),
                    Check.nullParam(this.position, "position"),
                    this.pitch,
                    this.yaw
            );
        }

    }

}
