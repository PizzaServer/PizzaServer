package io.github.pizzaserver.commons.utils;

public class NumberUtils {

    public static boolean isNearlyEqual(float a, float b) {
        return Math.abs(a - b) < 0.001f;
    }

    public static boolean isNearlyEqual(double a, double b) {
        return Math.abs(a - b) < 0.001d;
    }
}
