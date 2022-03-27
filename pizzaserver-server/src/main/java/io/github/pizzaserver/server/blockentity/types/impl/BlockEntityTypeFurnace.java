package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockBell;
import io.github.pizzaserver.api.block.impl.BlockFurnace;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityBell;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityFurnace;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BlockEntityTypeFurnace implements BlockEntityType<BlockFurnace> {

    private static final Set<String> BLOCK_TYPES = new HashSet<>() {
        {
            this.add(BlockID.FURNACE);
            this.add(BlockID.LIT_FURNACE);
        }
    };

    @Override
    public String getId() {
        return BlockEntityFurnace.ID;
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_TYPES;
    }

    @Override
    public BlockEntityFurnace create(BlockFurnace block) {
        return new BlockEntityFurnace(block);
    }

    @Override
    public BlockEntityFurnace deserializeDisk(Chunk chunk, NbtMap diskNBT) {
        Vector3i coordinates = Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z"));
        return new BlockEntityFurnace((BlockFurnace) chunk.getBlock(coordinates));
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity<BlockFurnace> blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getBlock().getX())
                .putInt("y", blockEntity.getBlock().getY())
                .putInt("z", blockEntity.getBlock().getZ())
                .putShort("CookTime", (short) 0)
                .putShort("BurnTime", (short) 0)
                .putShort("BurnDuration", (short) 0)
                .putList("Items", NbtType.COMPOUND, Collections.emptyList())
                .build();
        // TODO: proper serialization
    }

    @Override
    public NbtMap serializeForNetwork(NbtMap diskNBT) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", diskNBT.getInt("x"))
                .putInt("y", diskNBT.getInt("y"))
                .putInt("z", diskNBT.getInt("z"))
                .putShort("CookTime", (short) 0)
                .putShort("BurnTime", (short) 0)
                .putShort("BurnDuration", (short) 0)
                .putList("Items", NbtType.COMPOUND, Collections.emptyList())
                .build();
        // TODO: proper serialization
    }

}
