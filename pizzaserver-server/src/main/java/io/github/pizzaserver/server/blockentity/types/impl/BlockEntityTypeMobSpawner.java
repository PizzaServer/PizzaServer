package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockMobSpawner;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityMobSpawner;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Collections;
import java.util.Set;

public class BlockEntityTypeMobSpawner implements BlockEntityType<BlockMobSpawner> {

    @Override
    public String getId() {
        return BlockEntityMobSpawner.ID;
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.BELL);
    }

    @Override
    public BlockEntityMobSpawner create(BlockMobSpawner block) {
        return new BlockEntityMobSpawner(block);
    }

    @Override
    public BlockEntityMobSpawner deserializeDisk(Chunk chunk, NbtMap diskNBT) {
        Vector3i coordinates = Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z"));
        return new BlockEntityMobSpawner((BlockMobSpawner) chunk.getBlock(coordinates));
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity<BlockMobSpawner> blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getBlock().getX())
                .putInt("y", blockEntity.getBlock().getY())
                .putInt("z", blockEntity.getBlock().getZ())
                .putString("EntityIdentifier", "")
                .putFloat("DisplayEntityWidth", 1)
                .putFloat("DisplayEntityHeight", 1)
                .putFloat("DisplayEntityScale", 1)
                .build();
        // TODO: Implement proper serialization
    }

    @Override
    public NbtMap serializeForNetwork(NbtMap diskNBT) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", diskNBT.getInt("x"))
                .putInt("y", diskNBT.getInt("y"))
                .putInt("z", diskNBT.getInt("z"))
                .putInt("EntityId", 0)
                .putFloat("DisplayEntityWidth", 1)
                .putFloat("DisplayEntityHeight", 1)
                .putFloat("DisplayEntityScale", 1)
                .build();
        // TODO: Implement proper serialization
    }

}
