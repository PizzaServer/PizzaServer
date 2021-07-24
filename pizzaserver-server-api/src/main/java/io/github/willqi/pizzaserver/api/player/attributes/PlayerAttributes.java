package io.github.willqi.pizzaserver.api.player.attributes;

import java.util.Set;

public interface PlayerAttributes {

    /**
     * Retrieve a specific {@link Attribute}
     * @param attributeType attribute type
     * @return {@link Attribute}
     */
    Attribute getAttribute(AttributeType attributeType);

    /**
     * Update a attribute
     * @param attribute the {@link Attribute} to update
     */
    void setAttribute(Attribute attribute);

    /**
     * Retrieve all attributes of the player
     * @return {@link Set< Attribute >}
     */
    Set<Attribute> getAttributes();

}
