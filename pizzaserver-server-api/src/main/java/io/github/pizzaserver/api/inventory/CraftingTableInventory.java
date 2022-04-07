package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.block.impl.BlockCraftingTable;

public interface CraftingTableInventory extends BlockInventory<BlockCraftingTable>, CraftingInventory, TemporaryInventory {

}