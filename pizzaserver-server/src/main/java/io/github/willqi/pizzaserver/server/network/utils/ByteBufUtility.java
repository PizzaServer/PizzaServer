package io.github.willqi.pizzaserver.server.network.utils;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class ByteBufUtility {

    public static String readLEString(ByteBuf buffer) {
        int length = buffer.readIntLE();
        String data = buffer.readSlice(length).toString(StandardCharsets.UTF_8);
        return data;
    }

    public static String readString(ByteBuf buffer) {
        byte[] data = readByteArray(buffer);
        return new String(data, StandardCharsets.UTF_8);
    }

    public static void writeString(String string, ByteBuf buffer) {
        byte[] data = string.getBytes(StandardCharsets.UTF_8);
        writeByteArray(data, buffer);
    }

    public static byte[] readByteArray(ByteBuf buffer) {
        int length = VarInts.readUnsignedInt(buffer);
        byte[] data = new byte[length];
        buffer.readBytes(data);
        return data;
    }

    public static void writeByteArray(byte[] bytes, ByteBuf buffer) {
        VarInts.writeUnsignedInt(buffer, bytes.length);
        buffer.writeBytes(bytes);
    }

}
