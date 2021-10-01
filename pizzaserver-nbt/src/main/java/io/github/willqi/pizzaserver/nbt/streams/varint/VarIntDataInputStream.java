package io.github.willqi.pizzaserver.nbt.streams.varint;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Unlike Java, Bedrock uses VarInts to serialize their ints and longs.
 * VarInt implementation from Cloudburst's.
 * https://github.com/CloudburstMC/NBT/blob/master/src/main/java/com/nukkitx/nbt/util/VarInts.java
 * This is used when reading the NBT that is to be sent over the network.
 */
public class VarIntDataInputStream extends LittleEndianDataInputStream {

    public VarIntDataInputStream(InputStream stream) {
        super(stream);
    }

    @Override
    public int readInt() throws IOException {
        int value = ((int) this.decodeUnsigned());
        return (value >>> 1) ^ -(value & 1);
    }

    public int readUnsignedInt() throws IOException {
        return ((int) this.decodeUnsigned());
    }

    @Override
    public long readLong() throws IOException {
        long value = this.decodeUnsigned();
        return (value >>> 1) ^ -(value & 1);
    }

    @Override
    public String readUTF() throws IOException {
        byte[] data = new byte[this.readUnsignedInt()];
        this.readFully(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    // https://github.com/CloudburstMC/NBT/blob/master/src/main/java/com/nukkitx/nbt/util/VarInts.java#L45
    private long decodeUnsigned() throws IOException {
        long result = 0;
        for (int shift = 0; shift < 64; shift += 7) {
            byte b = this.readByte();
            result |= (long) (b & 0x7F) << shift;
            if ((b & 0x80) == 0) {
                return result;
            }
        }
        throw new ArithmeticException("Varint was too large");
    }
}
