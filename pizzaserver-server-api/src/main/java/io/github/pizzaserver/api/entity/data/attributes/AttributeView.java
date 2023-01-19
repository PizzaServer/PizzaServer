package io.github.pizzaserver.api.entity.data.attributes;

import com.nukkitx.protocol.bedrock.data.AttributeData;
import io.github.pizzaserver.commons.data.DataKey;
import io.github.pizzaserver.commons.data.ValueInterface;
import io.github.pizzaserver.commons.utils.Check;
import io.github.pizzaserver.commons.utils.NumberUtils;

public class AttributeView {

    private final DataKey<Float> type;
    private final ValueInterface<Float> minimumValue;
    private final ValueInterface<Float> maximumValue;
    private final ValueInterface<Float> defaultValue;
    private final ValueInterface<Float> currentValue;


    AttributeView(DataKey<Float> attributeType, ValueInterface<Float> minimumValue, ValueInterface<Float> maximumValue, ValueInterface<Float> defaultValue, ValueInterface<Float> currentValue) {
        this.type = Check.notNull(attributeType, "Attribute Type");

        this.minimumValue = Check.notNull(minimumValue, "Attribute: Min Value");
        this.maximumValue = Check.notNull(maximumValue, "Attribute: Max Value");
        this.defaultValue = Check.notNull(defaultValue, "Attribute: Default Value");
        this.currentValue = Check.notNull(currentValue, "Attribute: Current Value");
    }

    public DataKey<Float> getID() {
        return this.type;
    }

    public float getMinimumValue() {
        return this.minimumValue.getValue();
    }

    public void setMinimumValue(float minimumValue) {
        this.minimumValue.setValue(minimumValue);
    }

    public float getMaximumValue() {
        return this.maximumValue.getValue();
    }

    public void setMaximumValue(float maximumValue) {
        this.maximumValue.setValue(maximumValue);
        this.setDefaultValue(Math.max(this.getMinimumValue(), Math.min(this.getDefaultValue(), maximumValue)));
        this.setCurrentValue(Math.max(this.getMinimumValue(), Math.min(this.getCurrentValue(), maximumValue)));
    }

    public float getDefaultValue() {
        return this.defaultValue.getValue();
    }

    public void setDefaultValue(float defaultValue) {
        this.defaultValue.setValue(Math.max(this.getMinimumValue(), Math.min(defaultValue, this.getMaximumValue())));
    }

    public float getCurrentValue() {
        return this.currentValue.getValue();
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue.setValue(Math.max(this.getMinimumValue(), Math.min(currentValue, this.getMaximumValue())));
    }

    public AttributeData serialize() {
        return new AttributeData(this.getID().getKey(),
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
        if (obj instanceof AttributeView otherAttribute) {
            return otherAttribute.getID().equals(this.getID())
                    && NumberUtils.isNearlyEqual(otherAttribute.getCurrentValue(), this.getCurrentValue())
                    && NumberUtils.isNearlyEqual(otherAttribute.getDefaultValue(), this.getDefaultValue())
                    && NumberUtils.isNearlyEqual(otherAttribute.getMaximumValue(), this.getMaximumValue())
                    && NumberUtils.isNearlyEqual(otherAttribute.getMinimumValue(), this.getMinimumValue());
        }
        return false;
    }
}
