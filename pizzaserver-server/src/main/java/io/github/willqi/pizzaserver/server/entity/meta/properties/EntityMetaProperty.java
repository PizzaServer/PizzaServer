package io.github.willqi.pizzaserver.server.entity.meta.properties;

public class EntityMetaProperty<T> {

    private final T value;
    private final EntityMetaPropertyType type;


    public EntityMetaProperty(EntityMetaPropertyType type, T value) {
        this.type = type;
        this.value = value;
    }

    public EntityMetaPropertyType getType() {
        return this.type;
    }

    public T getValue() {
        return this.value;
    }

}
