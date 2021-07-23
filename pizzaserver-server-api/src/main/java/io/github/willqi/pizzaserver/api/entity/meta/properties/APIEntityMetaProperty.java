package io.github.willqi.pizzaserver.api.entity.meta.properties;

public interface APIEntityMetaProperty<T> {

    /**
     * Get the type of this property
     * @return a {@link EntityMetaPropertyType}
     */
    EntityMetaPropertyType getType();

    /**
     * Retrieve the value associated with this property
     * @return the value
     */
    T getValue();

}
