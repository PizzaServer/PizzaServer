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


    public static <T extends Number> Function<T, T> ensureAboveValue(T minimum) {
        return data -> minimum.floatValue() > data.floatValue()
                ? minimum
                : data;
    }

    public static <T extends Number> Function<T, T> ensureBelowValue(T maximum) {
        return data -> data.floatValue() > maximum.floatValue()
                ? maximum
                : data;
    }

    public static <T extends Number> Function<T, T> ensureAboveValue(ValueInterface<T> minimum) {
        return Preprocessors.ensureAboveValue(minimum.getValue());
    }

    public static <T extends Number> Function<T, T> ensureBelowValue(ValueInterface<T> maximum) {
        return Preprocessors.ensureBelowValue(maximum.getValue());
    }



    public static <T> Function<T, T> inOrder(Function<T, T>... stack) {
        return data -> {
            T feed = data;

            for (int i = 0; i < stack.length; i++)
                feed = stack[i].apply(feed);

            return feed;
        };
    }

}
