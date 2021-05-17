package io.github.willqi.pizzaserver.network.utils;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class ByteBufUtility {

    public static String readLEString(ByteBuf buffer) {
        int length = buffer.readIntLE();
        String data = buffer.readSlice(length).toString(StandardCharsets.UTF_8);
        return data;
    }

    public static void writeString(String string, ByteBuf buffer) {
        byte[] data = string.getBytes(StandardCharsets.UTF_8);
        VarInts.writeUnsignedInt(buffer, data.length);
        buffer.writeBytes(data);
    }

}
