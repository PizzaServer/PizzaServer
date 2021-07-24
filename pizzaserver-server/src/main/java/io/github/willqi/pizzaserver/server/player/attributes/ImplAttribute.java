package io.github.willqi.pizzaserver.server.player.attributes;

import io.github.willqi.pizzaserver.api.player.attributes.Attribute;
import io.github.willqi.pizzaserver.api.player.attributes.AttributeType;

public class ImplAttribute implements Attribute, Cloneable {

    private final AttributeType type;
    private float minimumValue;
    private float maximumValue;
    private float defaultValue;
    private float currentValue;


    public ImplAttribute(AttributeType attributeType, float minimumValue, float maximumValue, float defaultValue, float currentValue) {
        this.type = attributeType;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.defaultValue = defaultValue;
        this.currentValue = currentValue;
    }

    @Override
    public AttributeType getType() {
        return this.type;
    }

    @Override
    public float getMinimumValue() {
        return this.minimumValue;
    }

    @Override
    public void setMinimumValue(float minimumValue) {
        this.minimumValue = minimumValue;
    }

    @Override
    public float getMaximumValue() {
        return this.maximumValue;
    }

    @Override
    public void setMaximumValue(float maximumValue) {
        this.maximumValue = maximumValue;
        this.setDefaultValue(Math.max(this.getMinimumValue(), Math.min(this.getDefaultValue(), this.getMaximumValue())));
        this.setCurrentValue(Math.max(this.getMinimumValue(), Math.min(this.getCurrentValue(), this.getMaximumValue())));
    }

    @Override
    public float getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public void setDefaultValue(float defaultValue) {
        this.defaultValue = Math.max(this.getMinimumValue(), Math.min(defaultValue, this.getMaximumValue()));
    }

    @Override
    public float getCurrentValue() {
        return this.currentValue;
    }

    @Override
    public void setCurrentValue(float currentValue) {
        this.currentValue = Math.max(this.getMinimumValue(), Math.min(currentValue, this.getMaximumValue()));
    }

    @Override
    public int hashCode() {
        return 37 * this.type.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImplAttribute) {
            return ((ImplAttribute) obj).getType().equals(this.getType());
        }
        return false;
    }
}
