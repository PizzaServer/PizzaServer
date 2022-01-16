package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityFurnace;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BlockEntityTypeFurnace implements BlockEntityType {

    private static final Set<String> BLOCK_TYPES = new HashSet<>() {
        {
            this.add(BlockID.FURNACE);
            this.add(BlockID.LIT_FURNACE);
        }
    };

    @Override
    public String getId() {
        return BlockEntityFurnace.ID;
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_TYPES;
    }

    @Override
    public BlockEntityFurnace create(Block block) {
        return new BlockEntityFurnace(block.getLocation());
    }

    @Override
    public BlockEntityFurnace deserializeDisk(World world, NbtMap diskNBT) {
        return new BlockEntityFurnace(new BlockLocation(world,
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
                     .putShort("CookTime", (short) 0)
                     .putShort("BurnTime", (short) 0)
                     .putShort("BurnDuration", (short) 0)
                     .putList("Items", NbtType.COMPOUND, Collections.emptyList())
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
                     .putShort("CookTime", (short) 0)
                     .putShort("BurnTime", (short) 0)
                     .putShort("BurnDuration", (short) 0)
                     .putList("Items", NbtType.COMPOUND, Collections.emptyList())
                     .build();
        // TODO: proper serialization
    }
}
