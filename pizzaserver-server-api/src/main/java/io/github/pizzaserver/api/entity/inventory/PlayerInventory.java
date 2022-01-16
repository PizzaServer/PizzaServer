package io.github.pizzaserver.api.entity.inventory;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;

public interface PlayerInventory extends EntityInventory {

    Player getEntity();

    /**
     * Get the current selected slot for the player.
     *
     * @return current selected slot
     */
    int getSelectedSlot();

    /**
     * Change the current selected slot for the player.
     * This will not work for going from a empty slot to another empty slot
     *
     * @param slot new selected slot
     */
    void setSelectedSlot(int slot);

    /**
     * Get the item held by the cursor.
     *
     * @return item held by the player's cursor
     */
    Item getCursor();

    /**
     * Change the item held by the player's cursor.
     *
     * @param item item held by the player's cursor
     */
    void setCursor(Item item);
}
