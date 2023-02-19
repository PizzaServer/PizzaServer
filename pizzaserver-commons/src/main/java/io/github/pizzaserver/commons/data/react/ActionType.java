package io.github.pizzaserver.commons.data.react;

import io.github.pizzaserver.commons.data.key.DataKey;

public class ActionType<T> extends DataKey<T> {

    protected ActionType(String keyString, Class<T> valueType) {
        super(keyString, valueType);
    }

    public static <T> ActionType<T> of(String identifier, Class<T> value_type) {
        return new ActionType<>(identifier, value_type);
    }

}
