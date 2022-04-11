package io.github.pizzaserver.commons.utils;

public class NumberUtils {

    public static boolean isNearlyEqual(float a, float b) {
        return Math.abs(a - b) < 0.001f;
    }

    public static boolean isNearlyEqual(double a, double b) {
        return Math.abs(a - b) < 0.001d;
    }

    /**
     * Calculates the Base-2 logarithm of {@code bits}. If {@code bits} is zero, zero is returned.
     *
     * @param bits x
     * @return {@code ceil(log2(x))}
     */
    public static int log2Ceil(int bits) {
        if (bits == 0) {
            return 0;
        }
        return 31 - Integer.numberOfLeadingZeros(bits);
    }

}
