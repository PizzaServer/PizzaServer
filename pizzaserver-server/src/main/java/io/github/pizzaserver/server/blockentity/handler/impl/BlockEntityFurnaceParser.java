package io.github.pizzaserver.server.blockentity.handler.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import io.github.pizzaserver.api.blockentity.type.BlockEntityFurnace;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityFurnace;

import java.util.Collections;

public class BlockEntityFurnaceParser extends BaseBlockEntityParser<BlockEntityFurnace> {

    @Override
    public BlockEntityFurnace create(BlockLocation location) {
        return new ImplBlockEntityFurnace(location);
    }

    @Override
    public NbtMap toDiskNBT(BlockEntityFurnace blockEntity) {
        return super.toDiskNBT(blockEntity)
                .toBuilder()
                .putShort("CookTime", (short) 0)
                .putShort("BurnTime", (short) 0)
                .putShort("BurnDuration", (short) 0)
                .putList("Items", NbtType.COMPOUND, Collections.emptyList())
                .build();
    }

    @Override
    public NbtMap toNetworkNBT(BlockEntityFurnace blockEntity) {
        return super.toNetworkNBT(blockEntity)
                .toBuilder()
                .putShort("CookTime", (short) 0)
                .putShort("BurnTime", (short) 0)
                .putShort("BurnDuration", (short) 0)
                .build();
    }

    @Override
    public NbtMap toNetworkNBT(NbtMap diskNBT) {
        return super.toNetworkNBT(diskNBT)
                .toBuilder()
                .putShort("CookTime", (short) 0)
                .putShort("BurnTime", (short) 0)
                .putShort("BurnDuration", (short) 0)
                .build();
    }

}
