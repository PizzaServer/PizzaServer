package io.github.pizzaserver.api.event.type.inventory;

import io.github.pizzaserver.api.entity.inventory.Inventory;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.player.Player;

/**
 * Called when the player drops an item from an inventory.
 */
public class InventoryDropItemEvent extends BaseInventoryEvent.Cancellable {

    protected Player player;
    protected ItemStack drop;


    public InventoryDropItemEvent(Inventory inventory, Player player, ItemStack drop) {
        super(inventory);
        this.player = player;
        this.drop = drop;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ItemStack getDrop() {
        return this.drop;
    }

}
