package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.block.impl.BlockLegacyStoneCutter;
import io.github.pizzaserver.api.inventory.StoneCutterInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.recipe.data.RecipeBlockType;

public class ImplStoneCutterInventory extends ImplBlockInventory<BlockLegacyStoneCutter> implements StoneCutterInventory {

    private static final int SLOT_OFFSET = 3;

    public ImplStoneCutterInventory(BlockLegacyStoneCutter block) {
        super(block, ContainerType.STONECUTTER);
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getGridWidth() {
        return 1;
    }

    @Override
    public RecipeBlockType getRecipeBlockType() {
        return RecipeBlockType.STONECUTTER;
    }

    @Override
    public Item getInput() {
        return this.getSlot(0);
    }

    @Override
    public void setInput(Item item) {
        this.setSlot(0, item);
    }

    @Override
    public int convertFromNetworkSlot(int networkSlot) {
        // Despite the network slot offset being 3, when sending slots to the client, we do not need to add this offset.
        return networkSlot - SLOT_OFFSET;
    }

}
