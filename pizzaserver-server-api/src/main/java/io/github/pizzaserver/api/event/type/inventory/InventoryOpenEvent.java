package io.github.pizzaserver.api.event.type.inventory;

import io.github.pizzaserver.api.entity.inventory.Inventory;
import io.github.pizzaserver.api.player.Player;

/**
 * Called when an inventory is about to be opened for a player.
 */
public class InventoryOpenEvent extends BaseInventoryEvent.Cancellable {

    protected Player player;


    public InventoryOpenEvent(Player player, Inventory inventory) {
        super(inventory);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

}
