package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;

/**
 * Representative of the 2x2 crafting grid shown to players.
 */
public interface PlayerCraftingInventory extends CraftingInventory {

    Player getPlayer();

}
