package io.github.willqi.pizzaserver.server.player.attributes;

import io.github.willqi.pizzaserver.api.player.attributes.APIAttribute;
import io.github.willqi.pizzaserver.api.player.attributes.APIPlayerAttributes;
import io.github.willqi.pizzaserver.api.player.attributes.AttributeType;

import java.util.*;

public class PlayerAttributes implements APIPlayerAttributes {

    private final Map<AttributeType, APIAttribute> attributes = new HashMap<AttributeType, APIAttribute>(){
        {
            this.put(AttributeType.HEALTH, new Attribute(AttributeType.HEALTH, 0f, 20f, 20f, 20f));
            this.put(AttributeType.ABSORPTION, new Attribute(AttributeType.ABSORPTION, 0f, 0f, 0f, 0f));
            this.put(AttributeType.FOOD, new Attribute(AttributeType.FOOD, 0f, 20f, 20f, 20f));
            this.put(AttributeType.SATURATION, new Attribute(AttributeType.SATURATION, 0f, Float.MAX_VALUE, 0f, 0f));
            this.put(AttributeType.EXPERIENCE, new Attribute(AttributeType.EXPERIENCE, 0f, 1f, 0f, 0f));
            this.put(AttributeType.EXPERIENCE_LEVEL, new Attribute(AttributeType.EXPERIENCE_LEVEL, 0f, 24791f, 0f, 0f));
            this.put(AttributeType.MOVEMENT_SPEED, new Attribute(AttributeType.MOVEMENT_SPEED, 0f, Float.MAX_VALUE, 0.1f, 0.1f));
        }
    };


    @Override
    public APIAttribute getAttribute(AttributeType attributeType) {
        return this.attributes.get(attributeType);
    }

    @Override
    public void setAttribute(APIAttribute attribute) {
        this.attributes.put(attribute.getType(), attribute);
    }

    @Override
    public Set<APIAttribute> getAttributes() {
        return new HashSet<>(this.attributes.values());
    }

}
