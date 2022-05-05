package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockCampfire;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.item.Item;

public interface BlockEntityCampfire extends BlockEntity<BlockCampfire> {

    String ID = "Campfire";

    @Override
    default String getId() {
        return ID;
    }

    /**
     * Retrieve an item from the campfire's inventory.
     * @param slot slot between 0 and 3 inclusive.
     * @return the item
     */
    Item getItem(int slot);

    /**
     * Set an item in the campfire's inventory.
     * @param slot slot between 0 and 3 inclusive.
     * @param item the item
     */
    void setItem(int slot, Item item);

    /**
     * Get the amount of ticks left until a slot is done cooking.
     * @param slot slot between 0 and 3 inclusive.
     * @return ticks
     */
    int getCookTickProgressForSlot(int slot);

    /**
     * Set the amount of ticks left until a slot is done cooking.
     * @param slot slot between 0 and 3 inclusive.
     * @param cookTimeTicks ticks left until the slot is done cooking.
     */
    void setCookTickProgressForSlot(int slot, int cookTimeTicks);

}
