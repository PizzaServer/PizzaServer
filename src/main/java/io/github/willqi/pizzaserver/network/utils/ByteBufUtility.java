package io.github.willqi.pizzaserver.network.utils;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class ByteBufUtility {

    public static String readString(ByteBuf buffer) {
        int length = buffer.readIntLE();
        return buffer.readSlice(length).toString(StandardCharsets.UTF_8);
    }

}
