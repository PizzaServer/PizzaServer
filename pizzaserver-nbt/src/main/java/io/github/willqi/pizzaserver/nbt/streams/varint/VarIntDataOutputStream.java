package io.github.willqi.pizzaserver.nbt.streams.varint;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Unlike Java, Bedrock uses VarInts to serialize their ints and longs.
 * VarInt implementation from Cloudburst's.
 * https://github.com/CloudburstMC/NBT/blob/master/src/main/java/com/nukkitx/nbt/util/VarInts.java
 *
 * This is to be used when writing NBT that is to be sent over the network.
 */
public class VarIntDataOutputStream extends LittleEndianDataOutputStream {

    public VarIntDataOutputStream(OutputStream stream) {
        super(stream);
    }

    @Override
    public void writeInt(int v) throws IOException {
        this.writeEncoded(((long)v << 1) ^ (v >> 31));
    }

    public void writeUnsignedInt(int value) throws IOException {
        this.writeEncoded(value);
    }

    @Override
    public void writeLong(long v) throws IOException {
        this.writeEncoded((v << 1) ^ (v >> 63));
    }

    @Override
    public void writeUTF(String str) throws IOException {
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        this.writeUnsignedInt(data.length);
        this.write(data);
    }

    // https://github.com/CloudburstMC/NBT/blob/master/src/main/java/com/nukkitx/nbt/util/VarInts.java#L57
    private void writeEncoded(long value) throws IOException {
        while (true) {
            if ((value & ~0x7FL) == 0) {
                this.writeByte((int) value);
                return;
            } else {
                this.writeByte((byte) (((int) value & 0x7F) | 0x80));
                value >>>= 7;
            }
        }
    }

}
