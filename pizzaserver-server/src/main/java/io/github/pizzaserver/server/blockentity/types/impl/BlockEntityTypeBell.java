package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockBell;
import io.github.pizzaserver.api.block.impl.BlockCauldron;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityBell;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityCauldron;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Collections;
import java.util.Set;

public class BlockEntityTypeBell implements BlockEntityType<BlockBell> {

    @Override
    public String getId() {
        return BlockEntityBell.ID;
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.BELL);
    }

    @Override
    public BlockEntityBell create(BlockBell block) {
        return new BlockEntityBell(block);
    }

    @Override
    public BlockEntityBell deserializeDisk(Chunk chunk, NbtMap diskNBT) {
        Vector3i coordinates = Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z"));
        return new BlockEntityBell((BlockBell) chunk.getBlock(coordinates));
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity<BlockBell> blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getBlock().getX())
                .putInt("y", blockEntity.getBlock().getY())
                .putInt("z", blockEntity.getBlock().getZ())
                .putBoolean("Ringing", false)
                .putInt("Ticks", 0)
                .putInt("Direction", 0)
                .build();
        // TODO: proper serialization
    }

    @Override
    public NbtMap serializeForNetwork(NbtMap diskNBT) {
        return diskNBT;
    }

}
