package io.github.pizzaserver.server.blockentity.handler.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.blockentity.type.BlockEntityMobSpawner;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.definition.impl.EntityPigDefinition;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityMobSpawner;

public class BlockEntityMobSpawnerParser extends BaseBlockEntityParser<BlockEntityMobSpawner> {

    @Override
    public BlockEntityMobSpawner create(BlockLocation location) {
        return new ImplBlockEntityMobSpawner(location, EntityRegistry.getInstance().getDefinition(EntityPigDefinition.ID));
    }

    @Override
    public NbtMap toDiskNBT(BlockEntityMobSpawner blockEntity) {
        String entityId = "";
        if (blockEntity.getEntityDefinition().isPresent()) {
            entityId = blockEntity.getEntityDefinition().get().getEntityId();
        }

        return super.toDiskNBT(blockEntity)
                .toBuilder()
                .putString("EntityIdentifier", entityId)
                .putFloat("DisplayEntityWidth", 1)
                .putFloat("DisplayEntityHeight", 1)
                .putFloat("DisplayEntityScale", 1)
                .build();
    }

    @Override
    public NbtMap toNetworkNBT(NbtMap diskNBT) {
        return diskNBT;
    }

}