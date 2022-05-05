package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.block.impl.BlockLegacyStoneCutter;
import io.github.pizzaserver.api.item.Item;

public interface StoneCutterInventory extends BlockInventory<BlockLegacyStoneCutter>, CraftingInventory, TemporaryInventory {

    Item getInput();

    void setInput(Item item);

}
