package io.github.pizzaserver.server.blockentity.handler.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.blockentity.type.BlockEntityBed;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.api.utils.DyeColor;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityBed;

public class BlockEntityBedParser extends BaseBlockEntityParser<BlockEntityBed> {

    @Override
    public BlockEntityBed fromDiskNBT(World world, NbtMap nbt) {
        BlockLocation location = new BlockLocation(world,
                Vector3i.from(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")));

        BlockEntityBed blockEntityBed = this.create(location);
        blockEntityBed.setColor(DyeColor.values()[nbt.getByte("color")]);

        return blockEntityBed;
    }

    @Override
    public BlockEntityBed create(BlockLocation location) {
        return new ImplBlockEntityBed(location);
    }

    @Override
    public NbtMap toDiskNBT(BlockEntityBed blockEntity) {
        return super.toDiskNBT(blockEntity)
                .toBuilder()
                .putByte("color", (byte) blockEntity.getColor().ordinal())
                .build();
    }

    @Override
    public NbtMap toNetworkNBT(NbtMap diskNBT) {
        return diskNBT;
    }

}
