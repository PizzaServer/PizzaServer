package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockBed;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityBed;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.api.utils.DyeColor;

import java.util.Collections;
import java.util.Set;

public class BlockEntityTypeBed implements BlockEntityType<BlockBed> {

    @Override
    public String getId() {
        return BlockEntityBed.ID;
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.BED);
    }

    @Override
    public BlockEntityBed create(BlockBed block) {
        BlockEntityBed bedEntity = new BlockEntityBed(block);
        bedEntity.setColor(block.getColor());

        return bedEntity;
    }

    @Override
    public BlockEntityBed deserializeDisk(Chunk chunk, NbtMap diskNBT) {
        Vector3i coordinates = Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z"));
        BlockEntityBed blockEntity = new BlockEntityBed((BlockBed) chunk.getBlock(coordinates));

        blockEntity.setColor(DyeColor.values()[diskNBT.getByte("color")]);
        return blockEntity;
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getBlock().getX())
                .putInt("y", blockEntity.getBlock().getY())
                .putInt("z", blockEntity.getBlock().getZ())
                .putByte("color", (byte) ((BlockEntityBed) blockEntity).getColor().ordinal())
                .build();
    }

    @Override
    public NbtMap serializeForNetwork(NbtMap diskNBT) {
        return diskNBT;
    }

}
