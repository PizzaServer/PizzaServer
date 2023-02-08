package io.github.pizzaserver.commons.utils;

public class NumberUtils {

    public static boolean isNearlyEqual(float a, float b) {
        return Math.abs(a - b) < 0.001f;
    }

    public static boolean isNearlyEqual(double a, double b) {
        return Math.abs(a - b) < 0.001d;
    }

    public static <T extends Number> boolean isNearlyEqual(T a, T b) {
        return Math.abs(a.doubleValue() - b.doubleValue()) < 0.001d;
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

    /**
     * Compares 2 numbers and calculates the smallest value.
     * Unsafe for large numbers or incredibly precise numbers.
     *
     * @param numA the first number to compare
     * @param numB the second number to compare
     * @return the smallest of the 2 numbers, with an absolute error of 0.001
     * @param <T> the type of value compared.
     */
    public static <T extends Number> T approximateMin(T numA, T numB) {
        double a = numA.doubleValue();
        double b = numB.doubleValue();
        if(NumberUtils.isNearlyEqual(a, b)) return numA;

        return a < b
                ? numA
                : numB;
    }



    /**
     * Compares 2 numbers and calculates the largest value.
     * Unsafe for large numbers or incredibly precise numbers.
     *
     * @param numA the first number to compare
     * @param numB the second number to compare
     * @return the largest of the 2 numbers, with an absolute error of 0.001
     * @param <T> the type of value compared.
     */
    public static <T extends Number> T approximateMax(T numA, T numB) {
        double a = numA.doubleValue();
        double b = numB.doubleValue();
        if(NumberUtils.isNearlyEqual(a, b)) return numA;

        return a > b
                ? numA
                : numB;
    }

}
