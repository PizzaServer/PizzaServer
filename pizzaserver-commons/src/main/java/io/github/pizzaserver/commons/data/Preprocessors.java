package io.github.pizzaserver.commons.data;

import io.github.pizzaserver.commons.utils.Check;

import java.util.function.Function;

public class Preprocessors {

    public static <T> Function<T, T> nonNull(String name) {
        return data -> Check.notNull(data, name);
    }

    public static <T> Function<T, T> ifNullThen(T alternative) {
        return data -> Check.isNull(data) ? alternative : data;
    }

}
