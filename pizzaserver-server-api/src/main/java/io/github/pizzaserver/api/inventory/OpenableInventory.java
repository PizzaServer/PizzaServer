package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.player.Player;

public interface OpenableInventory extends Inventory {

    /**
     * Check if this player has permission to open this inventory by themselves.
     * @param player if the player has permission to open this inventory
     * @return if the player has permission to open the inventory by themselves
     */
    boolean canBeOpenedBy(Player player);

}
