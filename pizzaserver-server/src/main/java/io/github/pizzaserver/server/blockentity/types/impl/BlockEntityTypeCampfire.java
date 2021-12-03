package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityCampfire;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;

import java.util.HashSet;
import java.util.Set;

public class BlockEntityTypeCampfire implements BlockEntityType {

    private static final Set<BlockType> BLOCK_TYPES = new HashSet<BlockType>() {
        {
            this.add(BlockRegistry.getInstance().getBlockType(BlockTypeID.CAMPFIRE));
            this.add(BlockRegistry.getInstance().getBlockType(BlockTypeID.SOUL_CAMPFIRE));
        }
    };


    @Override
    public String getId() {
        return BlockEntityCampfire.ID;
    }

    @Override
    public Set<BlockType> getBlockTypes() {
        return BLOCK_TYPES;
    }

    @Override
    public BlockEntityCampfire create(Block block) {
        return new BlockEntityCampfire(block.getLocation().toVector3i());
    }

    @Override
    public BlockEntityCampfire deserialize(NbtMap diskNBT) {
        return new BlockEntityCampfire(Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z")));
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getPosition().getX())
                .putInt("y", blockEntity.getPosition().getY())
                .putInt("z", blockEntity.getPosition().getZ())
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
                .build();
        // TODO: proper serialization
    }

}
