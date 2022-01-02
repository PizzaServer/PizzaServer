package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityBlastFurnace;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BlockEntityTypeBlastFurnace extends BlockEntityTypeFurnace {

    private static final Set<BlockType> BLOCK_TYPES = new HashSet<BlockType>() {
        {
            this.add(BlockRegistry.getInstance().getBlockType(BlockTypeID.BLAST_FURNACE));
            this.add(BlockRegistry.getInstance().getBlockType(BlockTypeID.LIT_BLAST_FURNACE));
        }
    };

    @Override
    public String getId() {
        return BlockEntityBlastFurnace.ID;
    }

    @Override
    public Set<BlockType> getBlockTypes() {
        return BLOCK_TYPES;
    }

    @Override
    public BlockEntityBlastFurnace create(Block block) {
        return new BlockEntityBlastFurnace(block.getLocation());
    }

}
