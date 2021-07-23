package io.github.willqi.pizzaserver.server.entity.meta.properties;

import io.github.willqi.pizzaserver.api.entity.meta.properties.APIEntityMetaProperty;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyType;

public class EntityMetaProperty<T> implements APIEntityMetaProperty<T> {

    private final T value;
    private final EntityMetaPropertyType type;


    public EntityMetaProperty(EntityMetaPropertyType type, T value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public EntityMetaPropertyType getType() {
        return this.type;
    }

    @Override
    public T getValue() {
        return this.value;
    }

}
