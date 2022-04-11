package io.github.pizzaserver.server.blockentity.type.impl;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockFurnace;
import io.github.pizzaserver.api.blockentity.type.BlockEntityFurnace;
import io.github.pizzaserver.api.inventory.FurnaceInventory;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.inventory.ImplFurnaceInventory;

import java.util.Set;

public class ImplBlockEntityFurnace extends ImplBlockEntityContainer<BlockFurnace> implements BlockEntityFurnace {

    public static final Set<String> BLOCK_IDS = Set.of(BlockID.FURNACE, BlockID.LIT_FURNACE);

    public ImplBlockEntityFurnace(BlockLocation location) {
        super(location, ContainerType.FURNACE);
        this.inventory = new ImplFurnaceInventory(this);
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_IDS;
    }

    @Override
    public FurnaceInventory getInventory() {
        return (FurnaceInventory) this.inventory;
    }

}
