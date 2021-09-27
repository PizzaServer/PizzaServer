package io.github.willqi.pizzaserver.api.entity.meta.properties;

public class EntityMetaProperty<T> {

    private final T value;
    private final EntityMetaPropertyType type;


    public EntityMetaProperty(EntityMetaPropertyType type, T value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Get the type of this property.
     * @return a {@link EntityMetaPropertyType}
     */
    public EntityMetaPropertyType getType() {
        return this.type;
    }

    /**
     * Retrieve the value associated with this property.
     * @return the value
     */
    public T getValue() {
        return this.value;
    }

}
