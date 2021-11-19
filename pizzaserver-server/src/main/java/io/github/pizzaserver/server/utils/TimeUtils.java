package io.github.pizzaserver.server.utils;

public class TimeUtils {

    public static long secondsToNanoSeconds(int seconds) {
        return 1_000_000_000L * seconds;
    }

    public static long secondsToMilliseconds(int seconds) {
        return seconds * 1000L;
    }

    public static long nanoSecondsToMilliseconds(long nanoSeconds) {
        return nanoSeconds / 1000000L;
    }

}
