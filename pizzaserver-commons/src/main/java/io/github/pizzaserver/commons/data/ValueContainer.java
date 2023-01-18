package io.github.pizzaserver.commons.data;


import io.github.pizzaserver.commons.data.react.ActionSource;
import io.github.pizzaserver.commons.data.react.ActionType;
import io.github.pizzaserver.commons.utils.Check;

import java.util.function.Function;

public class ValueContainer<T> extends ActionSource {

    /** Returns the previous value of the container. */
    public static final ActionType<Object> ACTION_VALUE_PRE_SET = ActionType.of("value_set_pre", Object.class);

    /** Returns the value of the container once it has been set. */
    public static final ActionType<Object> ACTION_VALUE_SET = ActionType.of("value_set_post", Object.class);

    public static final ActionType<Void> ACTION_SET_STALE = ActionType.of("container_set_stale", Void.TYPE);


    private T value;
    private Function<T, T> preprocessor = data -> data;

    public void setValue(T value) {
        this.broadcast(ACTION_VALUE_PRE_SET, this.value);
        this.value = preprocessor.apply(value);
        this.broadcast(ACTION_VALUE_SET, this.value);
    }

    public ValueContainer<T> setPreprocessor(Function<T, T> preprocessor) {
        this.preprocessor = Check.notNull(preprocessor, "preprocessor");
        return this;
    }

    public T getValue() {
        return this.value;
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
