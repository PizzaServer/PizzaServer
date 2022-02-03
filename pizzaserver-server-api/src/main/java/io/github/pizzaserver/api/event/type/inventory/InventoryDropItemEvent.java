package io.github.pizzaserver.api.event.type.inventory;

import io.github.pizzaserver.api.entity.inventory.Inventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;

/**
 * Called when the player drops an item from an inventory.
 */
public class InventoryDropItemEvent extends BaseInventoryEvent.Cancellable {

    protected Player player;
    protected Item drop;


    public InventoryDropItemEvent(Inventory inventory, Player player, Item drop) {
        super(inventory);
        this.player = player;
        this.drop = drop;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Item getDrop() {
        return this.drop.clone();
    }

}
