package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.Location;

/**
 * Called when the player is respawning.
 */
public class PlayerRespawnEvent extends BasePlayerEvent {

    protected Location location;


    public PlayerRespawnEvent(Player player, Location location) {
        super(player);
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
