package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityCauldron;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Collections;
import java.util.Set;

public class BlockEntityTypeCauldron implements BlockEntityType {

    @Override
    public String getId() {
        return BlockEntityCauldron.ID;
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.CAULDRON);
    }

    @Override
    public BlockEntityCauldron create(Block block) {
        return new BlockEntityCauldron(block.getLocation());
    }

    @Override
    public BlockEntityCauldron deserializeDisk(World world, NbtMap diskNBT) {
        return new BlockEntityCauldron(new BlockLocation(world,
                Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z"))));
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getLocation().getX())
                .putInt("y", blockEntity.getLocation().getY())
                .putInt("z", blockEntity.getLocation().getZ())
                .putShort("PotionType", (short) -1)
                .putShort("PotionId", (short) -1)
                .putBoolean("IsSplash", false)
                .build();
        // TODO: proper serialization
    }

    @Override
    public NbtMap serializeForNetwork(NbtMap diskNBT) {
        return diskNBT;
    }

}
