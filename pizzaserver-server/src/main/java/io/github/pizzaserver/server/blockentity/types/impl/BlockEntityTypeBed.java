package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityBed;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;

import java.util.Collections;
import java.util.Set;

public class BlockEntityTypeBed implements BlockEntityType {

    @Override
    public String getId() {
        return BlockEntityBed.ID;
    }

    @Override
    public Set<BlockType> getBlockTypes() {
        return Collections.singleton(BlockRegistry.getInstance().getBlockType(BlockTypeID.BED));
    }

    @Override
    public BlockEntityBed create(Block block) {
        return new BlockEntityBed(block.getLocation().toVector3i());
    }

    @Override
    public BlockEntityBed deserialize(NbtMap diskNBT) {
        return new BlockEntityBed(Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z")));
        // TODO: bed color
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getPosition().getX())
                .putInt("y", blockEntity.getPosition().getY())
                .putInt("z", blockEntity.getPosition().getZ())
                .putByte("color", (byte) 0)
                .build();
        // TODO: proper serialization
    }

    @Override
    public NbtMap serializeForNetwork(BlockEntity blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getPosition().getX())
                .putInt("y", blockEntity.getPosition().getY())
                .putInt("z", blockEntity.getPosition().getZ())
                .putByte("color", (byte) 0)
                .build();
        // TODO: proper serialization
    }

}
