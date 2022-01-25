package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.Location;

/**
 * Called before the player entity is spawned.
 */
public class PlayerLoginEvent extends BasePlayerEvent.Cancellable {

    protected Location startLocation;

    public PlayerLoginEvent(Player player, Location startLocation) {
        super(player);
        this.startLocation = startLocation;
    }

    public Location getLocation() {
        return this.startLocation;
    }

    public void setLocation(Location location) {
        this.startLocation = location;
    }



}
