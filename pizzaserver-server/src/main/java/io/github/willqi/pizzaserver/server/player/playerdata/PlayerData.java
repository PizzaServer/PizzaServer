package io.github.willqi.pizzaserver.server.player.playerdata;

import io.github.willqi.pizzaserver.commons.utils.Check;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

public class PlayerData {

    private final String worldName;
    private final Vector3 position;
    private final float yaw;
    private final float pitch;


    private PlayerData(String worldName, Vector3 position, float yaw, float pitch) {
        this.worldName = worldName;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Retrieve the name of the {@link io.github.willqi.pizzaserver.api.world.World} the player is in
     * @return world name
     */
    public String getWorldName() {
        return this.worldName;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }


    public static class Builder {

        private String worldName;
        private Vector3 position;
        private float yaw;
        private float pitch;


        public Builder setWorldName(String worldName) {
            this.worldName = worldName;
            return this;
        }

        public Builder setPosition(Vector3 position) {
            this.position = position;
            return this;
        }

        public Builder setYaw(float yaw) {
            this.yaw = yaw;
            return this;
        }

        public Builder setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public PlayerData build() {
            return new PlayerData(
                    Check.nullParam(this.worldName, "worldName"),
                    Check.nullParam(this.position, "position"),
                    this.yaw,
                    this.pitch
            );
        }

    }

}
