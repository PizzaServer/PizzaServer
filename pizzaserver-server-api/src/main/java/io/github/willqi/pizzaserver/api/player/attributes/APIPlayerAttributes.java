package io.github.willqi.pizzaserver.api.player.attributes;

import java.util.Set;

public interface APIPlayerAttributes {

    /**
     * Retrieve a specific {@link APIAttribute}
     * @param attributeType attribute type
     * @return {@link APIAttribute}
     */
    APIAttribute getAttribute(AttributeType attributeType);

    /**
     * Update a attribute
     * @param attribute the {@link APIAttribute} to update
     */
    void setAttribute(APIAttribute attribute);

    /**
     * Retrieve all attributes of the player
     * @return {@link Set<APIAttribute>}
     */
    Set<APIAttribute> getAttributes();

}
