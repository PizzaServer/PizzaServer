package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityCampfire;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.HashSet;
import java.util.Set;

public class BlockEntityTypeCampfire implements BlockEntityType {

    private static final Set<String> BLOCK_TYPES = new HashSet<>() {
        {
            this.add(BlockID.CAMPFIRE);
            this.add(BlockID.SOUL_CAMPFIRE);
        }
    };


    @Override
    public String getId() {
        return BlockEntityCampfire.ID;
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_TYPES;
    }

    @Override
    public BlockEntityCampfire create(Block block) {
        return new BlockEntityCampfire(block.getLocation());
    }

    @Override
    public BlockEntity deserializeDisk(World world, NbtMap diskNBT) {
        return new BlockEntityCampfire(new BlockLocation(world,
                                                         Vector3i.from(diskNBT.getInt("x"),
                                                                       diskNBT.getInt("y"),
                                                                       diskNBT.getInt("z"))));
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity blockEntity) {
        return NbtMap.builder()
                     .putString("id", this.getId())
                     .putInt("x", blockEntity.getLocation().getX())
                     .putInt("y", blockEntity.getLocation().getY())
                     .putInt("z", blockEntity.getLocation().getZ())
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
                     .build();
        // TODO: proper serialization
    }
}
