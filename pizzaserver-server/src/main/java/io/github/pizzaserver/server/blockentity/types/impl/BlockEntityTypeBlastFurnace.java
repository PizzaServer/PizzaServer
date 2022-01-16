package io.github.pizzaserver.server.blockentity.types.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityBlastFurnace;

import java.util.HashSet;
import java.util.Set;

public class BlockEntityTypeBlastFurnace extends BlockEntityTypeFurnace {

    private static final Set<String> BLOCK_TYPES = new HashSet<>() {
        {
            this.add(BlockID.BLAST_FURNACE);
            this.add(BlockID.LIT_BLAST_FURNACE);
        }
    };

    @Override
    public String getId() {
        return BlockEntityBlastFurnace.ID;
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_TYPES;
    }

    @Override
    public BlockEntityBlastFurnace create(Block block) {
        return new BlockEntityBlastFurnace(block.getLocation());
    }
}
