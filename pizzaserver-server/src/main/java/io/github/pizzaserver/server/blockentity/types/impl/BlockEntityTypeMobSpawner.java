package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityMobSpawner;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;

import java.util.Collections;
import java.util.Set;

public class BlockEntityTypeMobSpawner implements BlockEntityType {

    @Override
    public String getId() {
        return BlockEntityMobSpawner.ID;
    }

    @Override
    public Set<BlockType> getBlockTypes() {
        return Collections.singleton(BlockRegistry.getInstance().getBlockType(BlockTypeID.BELL));
    }

    @Override
    public BlockEntityMobSpawner create(Block block) {
        return new BlockEntityMobSpawner(block.getLocation().toVector3i());
    }

    @Override
    public BlockEntityMobSpawner deserialize(NbtMap diskNBT) {
        return new BlockEntityMobSpawner(Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z")));
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getPosition().getX())
                .putInt("y", blockEntity.getPosition().getY())
                .putInt("z", blockEntity.getPosition().getZ())
                .putInt("EntityId", 0)
                .putFloat("DisplayEntityWidth", 1)
                .putFloat("DisplayEntityHeight", 1)
                .putFloat("DisplayEntityScale", 1)
                .build();
        // TODO: Implement proper serialization
    }

    @Override
    public NbtMap serializeForNetwork(BlockEntity blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getPosition().getX())
                .putInt("y", blockEntity.getPosition().getY())
                .putInt("z", blockEntity.getPosition().getZ())
                .putInt("EntityId", 0)
                .putFloat("DisplayEntityWidth", 1)
                .putFloat("DisplayEntityHeight", 1)
                .putFloat("DisplayEntityScale", 1)
                .build();
        // TODO: Implement proper serialization
    }

}
