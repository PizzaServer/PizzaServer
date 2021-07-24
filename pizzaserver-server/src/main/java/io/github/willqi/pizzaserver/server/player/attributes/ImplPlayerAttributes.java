package io.github.willqi.pizzaserver.server.player.attributes;

import io.github.willqi.pizzaserver.api.player.attributes.Attribute;
import io.github.willqi.pizzaserver.api.player.attributes.PlayerAttributes;
import io.github.willqi.pizzaserver.api.player.attributes.AttributeType;

import java.util.*;

public class ImplPlayerAttributes implements PlayerAttributes {

    private final Map<AttributeType, Attribute> attributes = new HashMap<AttributeType, Attribute>(){
        {
            this.put(AttributeType.HEALTH, new ImplAttribute(AttributeType.HEALTH, 0f, 20f, 20f, 20f));
            this.put(AttributeType.ABSORPTION, new ImplAttribute(AttributeType.ABSORPTION, 0f, 0f, 0f, 0f));
            this.put(AttributeType.FOOD, new ImplAttribute(AttributeType.FOOD, 0f, 20f, 20f, 20f));
            this.put(AttributeType.SATURATION, new ImplAttribute(AttributeType.SATURATION, 0f, Float.MAX_VALUE, 0f, 0f));
            this.put(AttributeType.EXPERIENCE, new ImplAttribute(AttributeType.EXPERIENCE, 0f, 1f, 0f, 0f));
            this.put(AttributeType.EXPERIENCE_LEVEL, new ImplAttribute(AttributeType.EXPERIENCE_LEVEL, 0f, 24791f, 0f, 0f));
            this.put(AttributeType.MOVEMENT_SPEED, new ImplAttribute(AttributeType.MOVEMENT_SPEED, 0f, Float.MAX_VALUE, 0.1f, 0.1f));
        }
    };


    @Override
    public Attribute getAttribute(AttributeType attributeType) {
        return this.attributes.get(attributeType);
    }

    @Override
    public void setAttribute(Attribute attribute) {
        this.attributes.put(attribute.getType(), attribute);
    }

    @Override
    public Set<Attribute> getAttributes() {
        return new HashSet<>(this.attributes.values());
    }

}
