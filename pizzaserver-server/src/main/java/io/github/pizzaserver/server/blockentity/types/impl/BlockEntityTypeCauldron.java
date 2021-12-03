package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityCauldron;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;

import java.util.Collections;
import java.util.Set;

public class BlockEntityTypeCauldron implements BlockEntityType {

    @Override
    public String getId() {
        return BlockEntityCauldron.ID;
    }

    @Override
    public Set<BlockType> getBlockTypes() {
        return Collections.singleton(BlockRegistry.getInstance().getBlockType(BlockTypeID.CAULDRON));
    }

    @Override
    public BlockEntityCauldron create(Block block) {
        return new BlockEntityCauldron(block.getLocation().toVector3i());
    }

    @Override
    public BlockEntityCauldron deserialize(NbtMap diskNBT) {
        return new BlockEntityCauldron(Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z")));
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getPosition().getX())
                .putInt("y", blockEntity.getPosition().getY())
                .putInt("z", blockEntity.getPosition().getZ())
                .putShort("PotionType", (short) -1)
                .putShort("PotionId", (short) -1)
                .putBoolean("IsSplash", false)
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
                .putShort("PotionType", (short) -1)
                .putShort("PotionId", (short) -1)
                .putBoolean("IsSplash", false)
                .build();
        // TODO: Cauldrons can have a custom RGB color
    }

}
