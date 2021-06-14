package io.github.willqi.pizzaserver.server.network.protocol.versions;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * Different versions of Minecraft encode packets differently.
 */
public class PacketHelper {

    public String readLEString(ByteBuf buffer) {
        int length = buffer.readIntLE();
        String data = buffer.readSlice(length).toString(StandardCharsets.UTF_8);
        return data;
    }

    public String readString(ByteBuf buffer) {
        byte[] data = readByteArray(buffer);
        return new String(data, StandardCharsets.UTF_8);
    }

    public void writeString(String string, ByteBuf buffer) {
        byte[] data = string.getBytes(StandardCharsets.UTF_8);
        writeByteArray(data, buffer);
    }

    public byte[] readByteArray(ByteBuf buffer) {
        int length = VarInts.readUnsignedInt(buffer);
        byte[] data = new byte[length];
        buffer.readBytes(data);
        return data;
    }

    public void writeByteArray(byte[] bytes, ByteBuf buffer) {
        VarInts.writeUnsignedInt(buffer, bytes.length);
        buffer.writeBytes(bytes);
    }

}
