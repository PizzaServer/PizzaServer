package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityFurnace;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BlockEntityTypeFurnace implements BlockEntityType {

    private static final Set<BlockType> BLOCK_TYPES = new HashSet<BlockType>() {
        {
            this.add(BlockRegistry.getInstance().getBlockType(BlockTypeID.FURNACE));
            this.add(BlockRegistry.getInstance().getBlockType(BlockTypeID.LIT_FURNACE));
        }
    };

    @Override
    public String getId() {
        return BlockEntityFurnace.ID;
    }

    @Override
    public Set<BlockType> getBlockTypes() {
        return BLOCK_TYPES;
    }

    @Override
    public BlockEntityFurnace create(Block block) {
        return new BlockEntityFurnace(block.getLocation().toVector3i());
    }

    @Override
    public BlockEntityFurnace deserialize(NbtMap diskNBT) {
        return new BlockEntityFurnace(Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z")));
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", blockEntity.getPosition().getX())
                .putInt("y", blockEntity.getPosition().getY())
                .putInt("z", blockEntity.getPosition().getZ())
                .putShort("CookTime", (short) 0)
                .putShort("BurnTime", (short) 0)
                .putShort("BurnDuration", (short) 0)
                .putList("Items", NbtType.COMPOUND, Collections.emptyList())
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
                .putShort("CookTime", (short) 0)
                .putShort("BurnTime", (short) 0)
                .putShort("BurnDuration", (short) 0)
                .putList("Items", NbtType.COMPOUND, Collections.emptyList())
                .build();
        // TODO: proper serialization
    }

}
