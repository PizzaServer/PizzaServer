package io.github.pizzaserver.commons.utils;

import io.github.pizzaserver.commons.exception.MissingPropertyException;

public final class Check {

    /**
     * For use in json, config, and Data API
     * situations where a specific property that is required
     * is missing.
     *
     * @param obj - The object which represents the property that is missing.
     * @param loc - A string descriptor of the holding object.
     * @param name - The name of the property.
     *
     * @return the parameter "obj"
     */
    public static <T> T missingProperty(T obj, String loc, String name) {
        if (isNull(obj)) {
            throw new MissingPropertyException(String.format("%s is missing a valid '%s' property.", loc, name));
        } else {
            return obj;
        }
    }

    /**
     * Checks if a parameter is null in a cleaner way, throwing an IllegalArgumentException if true.
     * @param obj - the parameter that is potentially null.
     * @param name - the name of the parameter.
     *
     * @return the parameter "obj"
     */
    public static <T> T nullParam(T obj, String name) {
        if (isNull(obj)) {
            throw new IllegalArgumentException(String.format("'%s' cannot be null.", name));
        } else {
            return obj;
        }
    }

    /**
     * Assets that a value is between two numbers.
     * @param val the value being checked.
     * @param lowerBound the lower bound checked (inclusive)
     * @param upperBound the upper bound checked (inclusive)
     * @param name the name of the variable/property.
     *
     * @return the parameter "val"
     */
    public static int inclusiveBounds(int val, int lowerBound, int upperBound, String name) {
        inclusiveLowerBound(val, lowerBound, name);
        inclusiveUpperBound(val, upperBound, name);
        return val;
    }

    public static int inclusiveLowerBound(int val, int bound, String name) {
        if (val < bound) {
            throw new IllegalStateException(String.format("'%s' is out of bounds (val = %s | Lower = %s)", name, val, bound));
        } else {
            return val;
        }
    }

    public static int inclusiveUpperBound(int val, int bound, String name) {
        if (val > bound) {
            throw new IllegalStateException(String.format("'%s' is out of bounds (val = %s | Upper = %s)", name, val, bound));
        } else {
            return val;
        }
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static void checkArgument(boolean mustBeTrue, String errorMessage) {
        if (!mustBeTrue) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

}
