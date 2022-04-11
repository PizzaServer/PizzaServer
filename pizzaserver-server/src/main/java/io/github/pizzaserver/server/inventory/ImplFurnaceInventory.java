package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.block.impl.BlockFurnace;
import io.github.pizzaserver.api.blockentity.type.BlockEntityFurnace;
import io.github.pizzaserver.api.inventory.FurnaceInventory;
import io.github.pizzaserver.api.item.Item;

public class ImplFurnaceInventory extends ImplContainerInventory<BlockFurnace, BlockEntityFurnace> implements FurnaceInventory {

    public ImplFurnaceInventory(BlockEntityFurnace blockEntity) {
        super(blockEntity, ContainerType.FURNACE);
    }

    @Override
    public Item getIngredient() {
        return this.getSlot(0);
    }

    @Override
    public void setIngredient(Item ingredient) {
        this.setSlot(0, ingredient);
    }

    @Override
    public Item getFuel() {
        return this.getSlot(1);
    }

    @Override
    public void setFuel(Item fuel) {
        this.setSlot(1, fuel);
    }

    @Override
    public Item getResult() {
        return this.getSlot(2);
    }

    @Override
    public void setResult(Item result) {
        this.setSlot(2, result);
    }

}
