package io.github.willqi.pizzaserver.server.player.playerdata;

import io.github.willqi.pizzaserver.commons.utils.Check;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.world.Dimension;

public class PlayerData {

    private final String levelName;
    private final Dimension dimension;
    private final Vector3 position;
    private final float pitch;
    private final float yaw;


    private PlayerData(String levelName,
                       Dimension dimension,
                       Vector3 position,
                       float pitch,
                       float yaw) {
        this.levelName = levelName;
        this.dimension = dimension;
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
                    Check.nullParam(this.position, "position"),
                    this.pitch,
                    this.yaw
            );
        }

    }

}
