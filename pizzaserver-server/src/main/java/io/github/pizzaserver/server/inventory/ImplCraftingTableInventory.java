package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.block.impl.BlockCraftingTable;
import io.github.pizzaserver.api.inventory.CraftingTableInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.utils.BlockLocation;

public class ImplCraftingTableInventory extends ImplBlockInventory<BlockCraftingTable> implements CraftingTableInventory {

    public ImplCraftingTableInventory(BlockLocation blockLocation, ContainerType containerType, int size) {

        super(blockLocation, containerType, size);
    }

    @Override
    public Item getResult() {

        return null;
    }

    @Override
    public Item[] getGridSlots() {

        return new Item[0];
    }

    @Override
    public Item getGridSlot(int slot) {

        return null;
    }

    @Override
    public void setGridSlots(Item[] grid) {

    }

    @Override
    public void setGridSlot(int slot, Item item) {

    }

}
