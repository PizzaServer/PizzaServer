package io.github.willqi.pizzaserver.api.event.type.player;

import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Location;

/**
 * Called after the client sends the SetLocalPlayerAsInitializedPacket but before the entity is spawned
 */
public class PlayerPreSpawnEvent extends BasePlayerEvent.Cancellable {

    protected Location startLocation;

    public PlayerPreSpawnEvent(Player player, Location startLocation) {
        super(player);
        this.startLocation = startLocation;
    }

    public Location getStartLocation() {
        return this.startLocation;
    }

    public void setStartLocation(Location location) {
        this.startLocation = startLocation;
    }



}
