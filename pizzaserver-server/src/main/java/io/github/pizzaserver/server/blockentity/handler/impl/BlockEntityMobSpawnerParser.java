package io.github.pizzaserver.server.blockentity.handler.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.blockentity.type.BlockEntityMobSpawner;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityMobSpawner;

public class BlockEntityMobSpawnerParser extends BaseBlockEntityParser<BlockEntityMobSpawner> {

    @Override
    public BlockEntityMobSpawner create(BlockLocation location) {
        return new ImplBlockEntityMobSpawner(location);
    }

    @Override
    public NbtMap toDiskNBT(BlockEntityMobSpawner blockEntity) {
        return super.toDiskNBT(blockEntity)
                .toBuilder()
                .putString("EntityIdentifier", "")
                .putFloat("DisplayEntityWidth", 1)
                .putFloat("DisplayEntityHeight", 1)
                .putFloat("DisplayEntityScale", 1)
                .build();
    }

    @Override
    public NbtMap toNetworkNBT(NbtMap diskNBT) {
        return super.toNetworkNBT(diskNBT)
                .toBuilder()
                .putInt("EntityId", 0)
                .putFloat("DisplayEntityWidth", 1)
                .putFloat("DisplayEntityHeight", 1)
                .putFloat("DisplayEntityScale", 1)
                .build();
    }

}
