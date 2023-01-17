package io.github.pizzaserver.commons.data;


import io.github.pizzaserver.commons.data.react.ActionSource;
import io.github.pizzaserver.commons.data.react.ActionType;

public class ValueContainer<T> extends ActionSource {

    /** Returns the previous value of the container. */
    public static ActionType<Object> ACTION_VALUE_PRE_SET = ActionType.of("value_set_pre", Object.class);

    /** Returns the value of the container once it has been set. */
    public static ActionType<Object> ACTION_VALUE_SET = ActionType.of("value_set_post", Object.class);

    public static ActionType<Void> ACTION_SET_STALE = ActionType.of("container_set_stale", Void.TYPE);


    private T value;

    public void setValue(T value) {
        this.broadcast(ACTION_VALUE_PRE_SET, this.value);
        this.value = value;
        this.broadcast(ACTION_VALUE_SET, this.value);
    }

    public T getValue() {
        return value;
    }

    /** Clears subscribers as this value should not be used*/
    void stale() {
        this.broadcast(ACTION_SET_STALE, null);
        this.clearSubscribers();
    }

    public static <T> ValueContainer<T> wrap(T value) {
        ValueContainer<T> valueContainer = new ValueContainer<>();
        valueContainer.setValue(value);

        return valueContainer;
    }

}
