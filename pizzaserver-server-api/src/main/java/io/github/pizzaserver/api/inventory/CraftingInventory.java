package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.item.Item;

public interface CraftingInventory extends Inventory {

    Item getResult();

    Item[] getGridSlots();

    Item getGridSlot(int slot);

    void setGridSlots(Item[] grid);

    void setGridSlot(int slot, Item item);

}
