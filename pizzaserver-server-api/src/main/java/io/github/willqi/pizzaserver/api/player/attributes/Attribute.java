package io.github.willqi.pizzaserver.api.player.attributes;

public interface Attribute {

    AttributeType getType();

    float getMinimumValue();

    void setMinimumValue(float minimumValue);

    float getMaximumValue();

    void setMaximumValue(float maximumValue);

    float getDefaultValue();

    void setDefaultValue(float defaultValue);

    float getCurrentValue();

    void setCurrentValue(float currentValue);

}
