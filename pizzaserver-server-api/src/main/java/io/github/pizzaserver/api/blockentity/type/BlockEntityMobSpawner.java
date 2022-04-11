package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockMobSpawner;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.definition.EntityDefinition;

import java.util.Optional;

public interface BlockEntityMobSpawner extends BlockEntity<BlockMobSpawner> {

    String ID = "MobSpawner";

    @Override
    default String getId() {
        return ID;
    }

    Optional<EntityDefinition> getEntityDefinition();

    default void setEntityDefinition(String entityId) {
        this.setEntityDefinition(EntityRegistry.getInstance().getDefinition(entityId));
    }

    void setEntityDefinition(EntityDefinition definition);

}
