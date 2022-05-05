package io.github.pizzaserver.api.inventory;

import io.github.pizzaserver.api.block.impl.BlockFurnace;
import io.github.pizzaserver.api.blockentity.type.BlockEntityFurnace;
import io.github.pizzaserver.api.item.Item;

public interface FurnaceInventory extends BlockEntityInventory<BlockFurnace, BlockEntityFurnace> {

    Item getIngredient();

    void setIngredient(Item ingredient);

    Item getFuel();

    void setFuel(Item fuel);

    Item getResult();

    void setResult(Item result);

}
