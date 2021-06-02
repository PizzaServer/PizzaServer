package io.github.willqi.pizzaserver.nbt.streams.varint;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Unlike Java, Bedrock uses VarInts to serialize their ints and longs.
 * VarInt implementation from Cloudburst's.
 * https://github.com/CloudburstMC/NBT/blob/master/src/main/java/com/nukkitx/nbt/util/VarInts.java
 */
public class VarIntDataOutputStream extends OutputStream implements DataOutput, Closeable {

    private final DataOutputStream stream;

    public VarIntDataOutputStream(OutputStream stream) {
        this.stream = new DataOutputStream(stream);
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }

    @Override
    public void write(int b) throws IOException {
        this.stream.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.stream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.stream.write(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        this.stream.writeBoolean(v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        this.stream.writeByte(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        this.stream.writeShort(Short.reverseBytes((short)v));
    }

    @Override
    public void writeChar(int v) throws IOException {
        this.stream.writeChar(Character.reverseBytes((char)v));
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
    public void writeFloat(float v) throws IOException {
        this.stream.writeInt(Integer.reverseBytes(Float.floatToIntBits(v)));
    }

    @Override
    public void writeDouble(double v) throws IOException {
        this.stream.writeLong(Long.reverseBytes(Double.doubleToLongBits(v)));
    }

    @Override
    public void writeBytes(String s) throws IOException {
        this.stream.writeBytes(s);
    }

    @Override
    public void writeChars(String s) throws IOException {
        this.stream.writeChars(s);
    }

    @Override
    public void writeUTF(String s) throws IOException {
        byte[] data = s.getBytes(StandardCharsets.UTF_8);
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
