package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.impl.BlockCraftingTable;
import io.github.pizzaserver.api.inventory.CraftingTableInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;

public class ImplCraftingTableInventory extends ImplBlockInventory<BlockCraftingTable> implements CraftingTableInventory {

    public ImplCraftingTableInventory(BlockCraftingTable block) {
        super(block, ContainerType.WORKBENCH, 4);
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

    @Override
    protected void sendContainerOpenPacket(Player player) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setId((byte) this.getId());
        containerOpenPacket.setType(ContainerType.WORKBENCH);
        containerOpenPacket.setBlockPosition(this.getBlock().getLocation().toVector3i());
        player.sendPacket(containerOpenPacket);
    }

}
