package io.github.pizzaserver.server.blockentity.handler.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.blockentity.type.BlockEntityCauldron;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityCauldron;

public class BlockEntityCauldronParser extends BaseBlockEntityParser<BlockEntityCauldron> {

    @Override
    public BlockEntityCauldron create(BlockLocation location) {
        return new ImplBlockEntityCauldron(location);
    }

    @Override
    public NbtMap toDiskNBT(BlockEntityCauldron blockEntity) {
        return super.toDiskNBT(blockEntity)
                .toBuilder()
                .putShort("PotionType", (short) -1)
                .putShort("PotionId", (short) -1)
                .putBoolean("IsSplash", false)
                .build();
    }

    @Override
    public NbtMap toNetworkNBT(NbtMap diskNBT) {
        return diskNBT;
    }

}
