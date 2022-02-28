package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.impl.BlockBed;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityBed;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.api.utils.DyeColor;

import java.util.Collections;
import java.util.Set;

public class BlockEntityTypeBed implements BlockEntityType<BlockBed, BlockEntityBed> {

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
        BlockEntityBed bedEntity = new BlockEntityBed(block.getLocation());
        bedEntity.setColor(block.getColor());

        return bedEntity;
    }

    @Override
    public BlockEntityBed deserializeDisk(World world, NbtMap diskNBT) {
        BlockEntityBed blockEntity = new BlockEntityBed(new BlockLocation(world,
                        Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z"))));

        blockEntity.setColor(DyeColor.values()[diskNBT.getByte("color")]);
        return blockEntity;
    }

    @Override
    public NbtMap serializeForDisk(BlockEntityBed bedEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", bedEntity.getLocation().getX())
                .putInt("y", bedEntity.getLocation().getY())
                .putInt("z", bedEntity.getLocation().getZ())
                .putByte("color", (byte) bedEntity.getColor().ordinal())
                .build();
    }

    @Override
    public NbtMap serializeForNetwork(NbtMap diskNBT) {
        return diskNBT;
    }

}
