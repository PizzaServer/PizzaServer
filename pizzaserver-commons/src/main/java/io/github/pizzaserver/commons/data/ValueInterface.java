package io.github.pizzaserver.commons.data;

public interface ValueInterface<T> {


    ValueInterface<T> setValue(T value);

    T getValue();

}
