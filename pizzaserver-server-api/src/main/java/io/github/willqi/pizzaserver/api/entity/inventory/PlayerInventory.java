package io.github.willqi.pizzaserver.api.entity.inventory;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.player.Player;

public interface PlayerInventory extends LivingEntityInventory {

    Player getEntity();

    /**
     * Get the current selected slot for the player
     * @return current selected slot
     */
    int getSelectedSlot();

    /**
     * Change the current selected slot for the player
     * This will not work for going from a empty slot to another empty slot
     * @param slot new selected slot
     * @return if the slot was changed
     */
    boolean setSelectedSlot(int slot);

    /**
     * Get the item held by the cursor
     * @return item held by the player's cursor
     */
    ItemStack getCursor();

    /**
     * Change the item held by the player's cursor
     * @param item item held by the player's cursor
     * @return if the slot was changed
     */
    boolean setCursor(ItemStack item);

}
