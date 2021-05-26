package io.github.willqi.pizzaserver.nbt.serializers.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtility {

    public static int readUnsignedInt(InputStream stream) throws IOException {
        return 0;
    }

    public static void putUnsignedInt(int value, OutputStream stream) throws IOException {

    }

    public static int readSignedInt(InputStream stream) throws IOException {
        return 0;
    }

    public static void putSignedInt(int value, OutputStream stream) throws IOException {

    }

    public static short readShort(InputStream stream) throws IOException {
        return 0;
    }

    public static void putShort(short value, OutputStream stream) throws IOException {

    }

    public static long readLong(InputStream stream) throws IOException {
        return 0;
    }

    public static void putLong(long value, OutputStream stream) throws IOException {

    }

    public static float readFloat(InputStream stream) throws IOException {
        return 0;
    }

    public static void putFloat(float value,OutputStream stream) throws IOException {

    }

    public static double readDouble(InputStream stream) throws IOException {
        return 0;
    }

    public static void putDouble(double value, OutputStream stream) throws IOException {

    }

    public static String readString(InputStream stream) throws IOException {
        return null;
    }

    public static void putString(String value, OutputStream stream) throws IOException {

    }

    public static String readName(InputStream stream) throws IOException {
        stream.skip(2);
        return readString(stream);
    }

    public static void putTagName(String name, OutputStream stream) throws IOException {
        putUnsignedInt(name.length(), stream);
        putString(name, stream);
    }





}
