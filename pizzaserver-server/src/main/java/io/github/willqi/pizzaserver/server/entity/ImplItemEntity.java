package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.entity.ItemEntity;
import io.github.willqi.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.willqi.pizzaserver.api.item.ItemStack;

public class ImplItemEntity extends ImplEntity implements ItemEntity {

    public ImplItemEntity(EntityDefinition entityDefinition) {
        super(entityDefinition);
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public ItemStack getItem() {
        return null;
    }

}
