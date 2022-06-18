package io.github.pizzaserver.server.entity;

import io.github.pizzaserver.api.entity.data.attributes.Attribute;
import io.github.pizzaserver.api.entity.data.attributes.AttributeType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityAttributes {

    private final Map<AttributeType, Attribute> attributes = new HashMap<>() {
        {
            this.put(AttributeType.HEALTH, new Attribute(AttributeType.HEALTH, 0f, 20f, 20f, 20f));
            this.put(AttributeType.ABSORPTION, new Attribute(AttributeType.ABSORPTION, 0f, 0f, 0f, 0f));
            this.put(AttributeType.FOOD, new Attribute(AttributeType.FOOD, 0f, 20f, 20f, 20f));
            this.put(AttributeType.SATURATION, new Attribute(AttributeType.SATURATION, 0f, Float.MAX_VALUE, 5f, 0f));
            this.put(AttributeType.EXPERIENCE, new Attribute(AttributeType.EXPERIENCE, 0f, 1f, 0f, 0f));
            this.put(AttributeType.EXPERIENCE_LEVEL, new Attribute(AttributeType.EXPERIENCE_LEVEL, 0f, 24791f, 0f, 0f));
            this.put(AttributeType.MOVEMENT_SPEED, new Attribute(AttributeType.MOVEMENT_SPEED, 0f, Float.MAX_VALUE, 0.1f, 0.1f));
        }
    };


    public Attribute getAttribute(AttributeType attributeType) {
        return this.attributes.get(attributeType);
    }

    public void setAttribute(Attribute attribute) {
        this.attributes.put(attribute.getType(), attribute);
    }

    public Set<Attribute> getAttributes() {
        return new HashSet<>(this.attributes.values());
    }

}
