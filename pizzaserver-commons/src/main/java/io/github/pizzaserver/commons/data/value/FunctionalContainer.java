package io.github.pizzaserver.commons.data.value;


import io.github.pizzaserver.commons.data.DataAction;

import java.util.function.Consumer;
import java.util.function.Supplier;

// Misusing ValueContainer a bit by ignoring the value completely on the getter.
// Optimizations welcome. :)
public class FunctionalContainer<T> extends ValueContainer<T> {

    private Consumer<T> setter;
    private Supplier<T> getter;

    public FunctionalContainer() {
        this.setter = null;
        this.getter = null;
    }

    @Override
    protected void internallyAssignValue(T value) {
        if(this.setter != null)
            this.setter.accept(value);
    }

    @Override
    public T getValue() {
        return this.getter == null
                ? null
                : this.getter.get();
    }

    // As no data is stored, nudging should do nothing.
    @Override
    public void nudge() {
        this.broadcast(DataAction.VALUE_SET, this.getter.get());
    }

    public FunctionalContainer<T> onGet(Supplier<T> getter) {
        this.getter = getter;
        return this;
    }

    public FunctionalContainer<T> onSet(Consumer<T> setter) {
        this.setter = setter;
        return this;
    }
}
