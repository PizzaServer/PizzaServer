package io.github.pizzaserver.commons.data;

import io.github.pizzaserver.commons.utils.Check;

import java.util.function.Function;
import java.util.function.Supplier;


public class Preprocessors {

    public static <T> Function<T, T> nonNull(String name) {
        return data -> Check.notNull(data, name);
    }

    public static <T> Function<T, T> ifNullThenValue(Supplier<T> alternative) {
        return data -> Check.isNull(data) ? alternative.get() : data;
    }

    public static <T> Function<T, T> ifNullThenConstant(T alternative) {
        return Preprocessors.ifNullThenValue(() -> alternative);
    }

    public static <T> Function<T, T> ifNullThenDefined(ValueProxy<T> alternative) {
        return Preprocessors.ifNullThenValue(alternative::getValue);
    }




    public static <T extends Number> Function<T, T> ensureAboveValue(Supplier<T> minimum) {
        T min = minimum.get();
        return data -> min.floatValue() > data.floatValue()
                ? min
                : data;
    }

    public static <T extends Number> Function<T, T> ensureAboveConstant(T minimum) {
        return Preprocessors.ensureAboveValue(() -> minimum);
    }

    public static <T extends Number> Function<T, T> ensureAboveDefined(ValueProxy<T> minimum) {
        return Preprocessors.ensureAboveValue(minimum::getValue);
    }




    public static <T extends Number> Function<T, T> ensureBelowValue(Supplier<T> maximum) {
        T max = maximum.get();
        return data -> data.floatValue() > max.floatValue()
                ? max
                : data;
    }

    public static <T extends Number> Function<T, T> ensureBelowConstant(T maximum) {
        return Preprocessors.ensureBelowValue(() -> maximum);
    }

    public static <T extends Number> Function<T, T> ensureBelowDefined(ValueProxy<T> maximum) {
        return Preprocessors.ensureBelowValue(maximum::getValue);
    }



    @SafeVarargs
    public static <T> Function<T, T> inOrder(Function<T, T>... stack) {
        return data -> {
            T feed = data;

            for (Function<T, T> ttFunction : stack)
                feed = ttFunction.apply(feed);

            return feed;
        };
    }

}
