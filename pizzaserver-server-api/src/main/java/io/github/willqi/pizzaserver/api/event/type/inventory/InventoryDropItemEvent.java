package io.github.willqi.pizzaserver.api.event.type.inventory;

import io.github.willqi.pizzaserver.api.entity.inventory.Inventory;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.player.Player;

/**
 * Called when the player drops an item from an inventory
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
