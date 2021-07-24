package io.github.willqi.pizzaserver.server.entity.meta.properties;

import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaProperty;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyType;

public class ImplEntityMetaProperty<T> implements EntityMetaProperty<T> {

    private final T value;
    private final EntityMetaPropertyType type;


    public ImplEntityMetaProperty(EntityMetaPropertyType type, T value) {
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
