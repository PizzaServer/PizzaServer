package io.github.willqi.pizzaserver.server.player.attributes;

public class Attribute implements Cloneable {

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
    }

    public float getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(float defaultValue) {
        this.defaultValue = defaultValue;
    }

    public float getCurrentValue() {
        return this.currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

}
