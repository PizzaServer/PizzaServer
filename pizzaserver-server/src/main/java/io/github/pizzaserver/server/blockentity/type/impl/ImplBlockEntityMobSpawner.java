package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockMobSpawner;
import io.github.pizzaserver.api.blockentity.type.BlockEntityMobSpawner;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityMobSpawner extends BaseBlockEntity<BlockMobSpawner> implements BlockEntityMobSpawner {

    private String entityId;

    public ImplBlockEntityMobSpawner(BlockLocation location, EntityDefinition definition) {
        super(location);
        this.entityId = definition.getEntityId();
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.MOB_SPAWNER);
    }

    @Override
    public EntityDefinition getEntityDefinition() {
        return EntityRegistry.getInstance().getDefinition(this.entityId);
    }

    @Override
    public void setEntityDefinition(EntityDefinition definition) {
        this.entityId = definition.getEntityId();
        this.update();
    }

}
