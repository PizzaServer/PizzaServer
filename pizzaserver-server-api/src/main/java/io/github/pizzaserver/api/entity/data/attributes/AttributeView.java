package io.github.pizzaserver.api.entity.data.attributes;

import com.nukkitx.protocol.bedrock.data.AttributeData;
import io.github.pizzaserver.commons.data.key.DataKey;
import io.github.pizzaserver.commons.data.value.ValueInterface;
import io.github.pizzaserver.commons.utils.Check;
import io.github.pizzaserver.commons.utils.NumberUtils;

public class AttributeView<T extends Number> {

    private final DataKey<T> type;
    private final ValueInterface<T> minimumValue;
    private final ValueInterface<T> maximumValue;
    private final ValueInterface<T> defaultValue;
    private final ValueInterface<T> currentValue;


    AttributeView(DataKey<T> attributeType, ValueInterface<T> minimumValue, ValueInterface<T> maximumValue, ValueInterface<T> defaultValue, ValueInterface<T> currentValue) {
        this.type = Check.notNull(attributeType, "Attribute Type");

        this.minimumValue = Check.notNull(minimumValue, "Attribute: Min Value");
        this.maximumValue = Check.notNull(maximumValue, "Attribute: Max Value");
        this.defaultValue = Check.notNull(defaultValue, "Attribute: Default Value");
        this.currentValue = Check.notNull(currentValue, "Attribute: Current Value");
    }

    public DataKey<T> getID() {
        return this.type;
    }

    public T getMinimumValue() {
        return this.minimumValue.getValue();
    }

    public void setMinimumValue(T minimumValue) {
        this.minimumValue.setValue(minimumValue);
    }

    public T getMaximumValue() {
        return this.maximumValue.getValue();
    }

    public void setMaximumValue(T maximumValue) {
        this.maximumValue.setValue(maximumValue);
        this.setDefaultValue(NumberUtils.approximateMax(this.getMinimumValue(), NumberUtils.approximateMin(this.getDefaultValue(), maximumValue)));
        this.setCurrentValue(NumberUtils.approximateMax(this.getMinimumValue(), NumberUtils.approximateMin(this.getCurrentValue(), maximumValue)));
    }

    public T getDefaultValue() {
        return this.defaultValue.getValue();
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue.setValue(NumberUtils.approximateMax(this.getMinimumValue(), NumberUtils.approximateMin(defaultValue, this.getMaximumValue())));
    }

    public T getCurrentValue() {
        return this.currentValue.getValue();
    }

    public void setCurrentValue(T currentValue) {
        this.currentValue.setValue(NumberUtils.approximateMax(this.getMinimumValue(), NumberUtils.approximateMin(currentValue, this.getMaximumValue())));
    }

    public AttributeData serialize() {
        return new AttributeData(this.getID().getKey(),
                (float) this.getMinimumValue(),
                (float) this.getMaximumValue(),
                (float) this.getCurrentValue(),
                (float) this.getDefaultValue());
    }

    @Override
    public int hashCode() {
        return 37 * this.type.hashCode()
                + (37 * (int) this.getCurrentValue())
                + (37 * (int) this.getDefaultValue())
                + (37 * (int) this.getMaximumValue())
                + (37 * (int) this.getMinimumValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AttributeView<?> otherAttribute) {
            return otherAttribute.getID().equals(this.getID())
                    && NumberUtils.isNearlyEqual(otherAttribute.getCurrentValue(), this.getCurrentValue())
                    && NumberUtils.isNearlyEqual(otherAttribute.getDefaultValue(), this.getDefaultValue())
                    && NumberUtils.isNearlyEqual(otherAttribute.getMaximumValue(), this.getMaximumValue())
                    && NumberUtils.isNearlyEqual(otherAttribute.getMinimumValue(), this.getMinimumValue());
        }
        return false;
    }
}
