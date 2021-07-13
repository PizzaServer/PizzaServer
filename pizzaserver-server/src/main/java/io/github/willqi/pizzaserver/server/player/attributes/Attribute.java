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

    @Override
    public int hashCode() {
        return 37 * this.type.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Attribute) {
            return ((Attribute) obj).getType().equals(this.getType());
        }
        return false;
    }
}
