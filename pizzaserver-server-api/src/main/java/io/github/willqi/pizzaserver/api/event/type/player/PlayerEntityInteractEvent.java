package io.github.willqi.pizzaserver.api.event.type.player;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.player.Player;

/**
 * Called when a player interacts with an entity.
 */
public class PlayerEntityInteractEvent extends BasePlayerEvent.Cancellable {

    protected Entity entity;


    public PlayerEntityInteractEvent(Player player, Entity entity) {
        super(player);
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }

}
