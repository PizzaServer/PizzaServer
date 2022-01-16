package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.Location;

/**
 * Called before the player entity is spawned.
 */
public class PlayerLoginEvent extends BasePlayerEvent.Cancellable {

    protected Location startLocation;
    protected float pitch;
    protected float yaw;

    public PlayerLoginEvent(Player player, Location startLocation, float pitch, float yaw) {
        super(player);
        this.startLocation = startLocation;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Location getStartLocation() {
        return this.startLocation;
    }

    public void setStartLocation(Location location) {
        this.startLocation = location;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
