package io.github.pizzaserver.commons.data.value;

public interface ValueInterface<T> {


    ValueInterface<T> setValue(T value);

    T getValue();

    void nudge();

}
