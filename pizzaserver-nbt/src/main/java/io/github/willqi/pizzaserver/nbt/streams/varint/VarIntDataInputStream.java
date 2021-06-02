package io.github.willqi.pizzaserver.nbt.streams.varint;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Unlike Java, Bedrock uses VarInts to serialize their ints and longs.
 * VarInt implementation from Cloudburst's.
 * https://github.com/CloudburstMC/NBT/blob/master/src/main/java/com/nukkitx/nbt/util/VarInts.java
 */
public class VarIntDataInputStream extends InputStream implements DataInput, Closeable {

    private final DataInputStream stream;

    public VarIntDataInputStream(InputStream stream) {
        this.stream = new DataInputStream(stream);
    }

    @Override
    public int available() throws IOException {
        return this.stream.available();
    }

    @Override
    public int read() throws IOException {
        return this.stream.read();
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        this.stream.readFully(b);
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        this.stream.readFully(b, off, len);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        return this.stream.skipBytes(n);
    }

    @Override
    public boolean readBoolean() throws IOException {
        return this.stream.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return this.stream.readByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return this.stream.readUnsignedByte();
    }

    @Override
    public short readShort() throws IOException {
        return Short.reverseBytes(this.stream.readShort());
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return Short.toUnsignedInt(Short.reverseBytes(this.stream.readShort()));
    }

    @Override
    public char readChar() throws IOException {
        return Character.reverseBytes(this.stream.readChar());
    }

    @Override
    public int readInt() throws IOException {
        int value = (int)this.decodeUnsigned();
        return (value >>> 1) ^ -(value & 1);
    }

    public int readUnsignedInt() throws IOException {
        return (int)this.decodeUnsigned();
    }

    @Override
    public long readLong() throws IOException {
        long value = this.decodeUnsigned();
        return (value >>> 1) ^ -(value & 1);
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(Integer.reverseBytes(this.stream.readInt()));
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(Long.reverseBytes(this.stream.readLong()));
    }

    @Override
    public String readLine() throws IOException {
        return this.stream.readLine();
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
