package io.github.pizzaserver.commons.data.value;

import io.github.pizzaserver.commons.utils.Check;

public final class ValueProxy<T> implements ValueInterface<T> {

    private final ValueContainer<T> source;

    public ValueProxy(ValueContainer<T> source) {
        this.source = Check.notNull(source, "Proxy Source");
    }


    @Override
    public ValueProxy<T> setValue(T value) {
        this.source.setValue(value);
        return this;
    }

    @Override
    public T getValue() {
        return this.source.getValue();
    }
}
