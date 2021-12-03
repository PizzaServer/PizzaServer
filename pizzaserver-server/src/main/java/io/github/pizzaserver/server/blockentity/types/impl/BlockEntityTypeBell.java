package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityBell;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;

import java.util.Collections;
import java.util.Set;

public class BlockEntityTypeBell implements BlockEntityType {

    @Override
    public String getId() {
        return BlockEntityBell.ID;
    }

    @Override
    public Set<BlockType> getBlockTypes() {
        return Collections.singleton(BlockRegistry.getInstance().getBlockType(BlockTypeID.BELL));
    }

    @Override
    public BlockEntityBell create(Block block) {
        return new BlockEntityBell(block.getLocation().toVector3i());
    }

    @Override
    public BlockEntityBell deserialize(NbtMap diskNBT) {
        return new BlockEntityBell(Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z")));
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getPosition().getX())
                .putInt("y", blockEntity.getPosition().getY())
                .putInt("z", blockEntity.getPosition().getZ())
                .putBoolean("Ringing", false)
                .putInt("Ticks", 0)
                .putInt("Direction", 0)
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
                .putBoolean("Ringing", false)
                .putInt("Ticks", 0)
                .putInt("Direction", 0)
                .build();
        // TODO: proper serialization
    }

}
