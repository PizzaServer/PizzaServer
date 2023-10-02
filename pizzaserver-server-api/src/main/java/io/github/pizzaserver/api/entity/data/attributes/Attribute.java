package io.github.pizzaserver.api.entity.data.attributes;

import io.github.pizzaserver.commons.utils.NumberUtils;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;

public class Attribute {

    private final AttributeType type;
    private float minimumValue;
    private float maximumValue;
    private float defaultValue;
    private float currentValue;


    public Attribute(AttributeType attributeType, float minimumValue, float maximumValue, float defaultValue, float currentValue) {
        this.type = attributeType;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.defaultValue = defaultValue;
        this.currentValue = currentValue;
    }

    public AttributeType getType() {
        return this.type;
    }

    public float getMinimumValue() {
        return this.minimumValue;
    }

    public void setMinimumValue(float minimumValue) {
        this.minimumValue = minimumValue;
    }

    public float getMaximumValue() {
        return this.maximumValue;
    }

    public void setMaximumValue(float maximumValue) {
        this.maximumValue = maximumValue;
        this.setDefaultValue(Math.max(this.getMinimumValue(), Math.min(this.getDefaultValue(), this.getMaximumValue())));
        this.setCurrentValue(Math.max(this.getMinimumValue(), Math.min(this.getCurrentValue(), this.getMaximumValue())));
    }

    public float getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(float defaultValue) {
        this.defaultValue = Math.max(this.getMinimumValue(), Math.min(defaultValue, this.getMaximumValue()));
    }

    public float getCurrentValue() {
        return this.currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = Math.max(this.getMinimumValue(), Math.min(currentValue, this.getMaximumValue()));
    }

    public AttributeData serialize() {
        return new AttributeData(this.getType().getId(),
                this.getMinimumValue(),
                this.getMaximumValue(),
                this.getCurrentValue(),
                this.getDefaultValue());
    }

    @Override
    public int hashCode() {
        return ((int) (37 * this.type.hashCode()
                + (37 * this.getCurrentValue())
                + (37 * this.getDefaultValue())
                + (37 * this.getMaximumValue())
                + (37 * this.getMinimumValue())));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Attribute) {
            Attribute otherAttribute = (Attribute) obj;
            return otherAttribute.getType().equals(this.getType())
                    && NumberUtils.isNearlyEqual(otherAttribute.getCurrentValue(), this.getCurrentValue())
                    && NumberUtils.isNearlyEqual(otherAttribute.getDefaultValue(), this.getDefaultValue())
                    && NumberUtils.isNearlyEqual(otherAttribute.getMaximumValue(), this.getMaximumValue())
                    && NumberUtils.isNearlyEqual(otherAttribute.getMinimumValue(), this.getMinimumValue());
        }
        return false;
    }
}
