package io.github.pizzaserver.commons.data.value;

import io.github.pizzaserver.commons.data.react.ActionSource;
import io.github.pizzaserver.commons.data.react.ActionType;
import io.github.pizzaserver.commons.utils.Check;

import java.util.function.Consumer;

public final class ValueProxy<T> implements ValueInterface<T>, ActionSource {

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

    @Override
    public void nudge() {
        this.source.nudge();
    }



    @Override
    public <L> void listenFor(ActionType<L> action, Consumer<L> callback) {
        this.source.listenFor(action, callback);
    }

    @Override
    public <L> void listenFor(ActionType<L> action, Runnable callback) {
        this.source.listenFor(action, callback);
    }

    @Override
    public void stopListeningFor(ActionType<?> action, Consumer<?> callback) {
        this.source.stopListeningFor(action, callback);
    }

    @Override
    public void stopListeningFor(ActionType<?> action, Runnable callback) {
        this.source.stopListeningFor(action, callback);
    }
}
