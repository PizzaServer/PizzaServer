package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.impl.BlockBlastFurnace;
import io.github.pizzaserver.api.block.impl.BlockFurnace;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityBlastFurnace;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityFurnace;
import io.github.pizzaserver.api.level.world.chunks.Chunk;

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
    public BlockEntityBlastFurnace create(BlockFurnace block) {
        return new BlockEntityBlastFurnace((BlockBlastFurnace) block);
    }

    @Override
    public BlockEntityFurnace deserializeDisk(Chunk chunk, NbtMap diskNBT) {
        Vector3i coordinates = Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z"));
        return new BlockEntityBlastFurnace((BlockBlastFurnace) chunk.getBlock(coordinates));
    }

}
