package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockFurnace;
import io.github.pizzaserver.api.blockentity.type.BlockEntityFurnace;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

import java.util.Set;

public class ImplBlockEntityFurnace extends BaseBlockEntity<BlockFurnace> implements BlockEntityFurnace {

    public static final Set<String> BLOCK_IDS = Set.of(BlockID.FURNACE, BlockID.LIT_FURNACE);

    public ImplBlockEntityFurnace(BlockLocation location) {
        super(location);
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_IDS;
    }

}
