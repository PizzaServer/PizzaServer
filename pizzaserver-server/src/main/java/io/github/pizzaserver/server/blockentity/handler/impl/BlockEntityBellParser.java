package io.github.pizzaserver.server.blockentity.handler.impl;

import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.blockentity.type.BlockEntityBell;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityBell;

public class BlockEntityBellParser extends BaseBlockEntityParser<BlockEntityBell> {

    @Override
    public BlockEntityBell create(BlockLocation location) {
        return new ImplBlockEntityBell(location);
    }

    @Override
    public NbtMap toDiskNBT(BlockEntityBell blockEntity) {
        return super.toDiskNBT(blockEntity).toBuilder()
                .putBoolean("Ringing", blockEntity.isRinging())
                .putInt("Ticks", 0)
                .putInt("Direction", 0)
                .build();
    }

    @Override
    public NbtMap toNetworkNBT(NbtMap diskNBT) {
        return diskNBT;
    }

}
