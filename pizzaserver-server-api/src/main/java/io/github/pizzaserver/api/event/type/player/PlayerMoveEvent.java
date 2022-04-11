package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.Location;

/**
 * Called whenever a player moves their player location/camera.
 */
public class PlayerMoveEvent extends BasePlayerEvent.Cancellable {

    protected final Location oldLocation;
    protected final Location newLocation;

    public PlayerMoveEvent(Player player, Location oldLocation, Location newLocation) {
        super(player);
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
    }

    public Location getFrom() {
        return this.oldLocation;
    }

    public Location getTo() {
        return this.newLocation;
    }

}
