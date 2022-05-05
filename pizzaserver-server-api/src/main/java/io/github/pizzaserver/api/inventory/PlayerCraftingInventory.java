package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.player.Player;

/**
 * Representative of the 2x2 crafting grid shown to players.
 */
public interface PlayerCraftingInventory extends CraftingInventory, TemporaryInventory {

    Player getPlayer();

    @Override
    default int getGridHeight() {
        return 2;
    }

    @Override
    default int getGridWidth() {
        return 2;
    }

}
