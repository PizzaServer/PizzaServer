package io.github.willqi.pizzaserver.api.entity.inventory;

import io.github.willqi.pizzaserver.api.item.ItemStack;

public interface PlayerInventory extends LivingEntityInventory {

    /**
     * Get the current selected slot for the player
     * @return current selected slot
     */
    int getSelectedSlot();

    /**
     * Change the current selected slot for the player
     * @param slot new selected slot
     */
    void setSelectedSlot(int slot);

    /**
     * Get the item held by the cursor
     * @return item held by the player's cursor
     */
    ItemStack getCursor();

    /**
     * Change the item held by the player's cursor
     * @param item item held by the player's cursor
     */
    void setCursor(ItemStack item);

}
