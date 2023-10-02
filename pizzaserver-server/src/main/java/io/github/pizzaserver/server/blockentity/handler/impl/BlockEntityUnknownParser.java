package io.github.pizzaserver.server.blockentity.handler.impl;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.type.BlockEntityUnknown;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityUnknown;

public class BlockEntityUnknownParser<T extends Block> extends BaseBlockEntityParser<BlockEntityUnknown<T>> {

    @Override
    public BlockEntityUnknown<T> fromDiskNBT(World world, NbtMap nbt) {
        BlockLocation location = new BlockLocation(world,
                Vector3i.from(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")));

        return new ImplBlockEntityUnknown<>(nbt, location);
    }

    @Override
    public NbtMap toDiskNBT(BlockEntityUnknown<T> blockEntity) {
        return ((ImplBlockEntityUnknown<T>) blockEntity).getData();
    }

    @Override
    public NbtMap toNetworkNBT(NbtMap diskNBT) {
        return diskNBT;
    }

    @Override
    public BlockEntityUnknown<T> create(BlockLocation location) {
        throw new UnsupportedOperationException("Cannot create unknown block entity from just a location.");
    }

}
