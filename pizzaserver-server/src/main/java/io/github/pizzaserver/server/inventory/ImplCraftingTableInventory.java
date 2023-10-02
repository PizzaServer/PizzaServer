package io.github.pizzaserver.server.inventory;

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerId;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import org.cloudburstmc.protocol.bedrock.packet.ContainerOpenPacket;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.impl.BlockCraftingTable;
import io.github.pizzaserver.api.inventory.CraftingTableInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.recipe.data.RecipeBlockType;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Set;

public class ImplCraftingTableInventory extends ImplBlockInventory<BlockCraftingTable> implements CraftingTableInventory {

    private static final int CRAFTING_SLOT_OFFSET = 32;


    public ImplCraftingTableInventory(BlockCraftingTable block) {
        super(block, ContainerType.WORKBENCH);
    }

    @Override
    protected void sendContainerOpenPacket(Player player) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setId((byte) this.getId());
        containerOpenPacket.setType(ContainerType.WORKBENCH);
        containerOpenPacket.setBlockPosition(this.getBlock().getLocation().toVector3i());
        player.sendPacket(containerOpenPacket);
    }

    @Override
    public void sendSlots(Player player) {
        for (int slot = 0; slot < 9; slot++) {
            this.sendSlot(player, slot);
        }
    }

    @Override
    public void sendSlot(Player player, int slot) {
        if (this.getViewers().contains(player)) {
            sendInventorySlot(player, this.getSlot(slot), slot + CRAFTING_SLOT_OFFSET, ContainerId.UI);
        }
    }

    @Override
    public int convertFromNetworkSlot(int networkSlot) {
        return networkSlot - CRAFTING_SLOT_OFFSET;
    }

    @Override
    public RecipeBlockType getRecipeBlockType() {
        return RecipeBlockType.CRAFTING_TABLE;
    }

}
