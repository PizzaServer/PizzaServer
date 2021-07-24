package io.github.willqi.pizzaserver.server.player.attributes;

import io.github.willqi.pizzaserver.api.player.attributes.Attribute;
import io.github.willqi.pizzaserver.api.player.attributes.PlayerAttributes;
import io.github.willqi.pizzaserver.api.player.attributes.AttributeType;

import java.util.*;

public class BedrockPlayerAttributes implements PlayerAttributes {

    private final Map<AttributeType, Attribute> attributes = new HashMap<AttributeType, Attribute>(){
        {
            this.put(AttributeType.HEALTH, new BedrockAttribute(AttributeType.HEALTH, 0f, 20f, 20f, 20f));
            this.put(AttributeType.ABSORPTION, new BedrockAttribute(AttributeType.ABSORPTION, 0f, 0f, 0f, 0f));
            this.put(AttributeType.FOOD, new BedrockAttribute(AttributeType.FOOD, 0f, 20f, 20f, 20f));
            this.put(AttributeType.SATURATION, new BedrockAttribute(AttributeType.SATURATION, 0f, Float.MAX_VALUE, 0f, 0f));
            this.put(AttributeType.EXPERIENCE, new BedrockAttribute(AttributeType.EXPERIENCE, 0f, 1f, 0f, 0f));
            this.put(AttributeType.EXPERIENCE_LEVEL, new BedrockAttribute(AttributeType.EXPERIENCE_LEVEL, 0f, 24791f, 0f, 0f));
            this.put(AttributeType.MOVEMENT_SPEED, new BedrockAttribute(AttributeType.MOVEMENT_SPEED, 0f, Float.MAX_VALUE, 0.1f, 0.1f));
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
