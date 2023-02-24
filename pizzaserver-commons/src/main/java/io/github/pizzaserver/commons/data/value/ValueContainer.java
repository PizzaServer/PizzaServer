package io.github.pizzaserver.commons.data.value;


import io.github.pizzaserver.commons.data.DataAction;
import io.github.pizzaserver.commons.data.react.ActionRootSource;
import io.github.pizzaserver.commons.utils.Check;

import java.util.function.Function;

public class ValueContainer<T> extends ActionRootSource implements ValueInterface<T> {

    private T value;
    private Function<T, T> preprocessor = data -> data;

    // Use ValueContainer#wrap()
    protected ValueContainer() { }


    @Override
    public ValueContainer<T> setValue(T value) {
        this.broadcast(DataAction.VALUE_PRE_SET, this.value);
        this.internallyAssignValue(preprocessor.apply(value));
        this.broadcast(DataAction.VALUE_SET, this.value);
        return this;
    }

    protected void internallyAssignValue(T value) {
        this.value = value;
    }


    @Override
    public T getValue() {
        return this.value;
    }

    public ValueContainer<T> setPreprocessor(Function<T, T> preprocessor) {
        this.preprocessor = Check.notNull(preprocessor, "preprocessor");
        return this;
    }

    /** Forces a container to trigger preprocessors + listeners without setting a new value. */
    public void nudge() {
        this.setValue(this.value);
    }


    /** Clears subscribers as this value should not be used*/
    public void stale() {
        this.broadcast(DataAction.CONTAINER_STALE, null);
        this.clearSubscribers();
    }

    public static <T> ValueContainer<T> wrap(T value) {
        ValueContainer<T> valueContainer = new ValueContainer<>();
        valueContainer.setValue(value);

        return valueContainer;
    }
}
