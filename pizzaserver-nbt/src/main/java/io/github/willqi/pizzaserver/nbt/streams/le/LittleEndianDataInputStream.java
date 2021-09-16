package io.github.willqi.pizzaserver.nbt.streams.le;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Bedrock NBT reading stream for NBT read from the disk.
 */
public class LittleEndianDataInputStream extends InputStream implements DataInput {

    protected final DataInputStream stream;


    public LittleEndianDataInputStream(InputStream stream) {
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
        return Integer.reverseBytes(this.stream.readInt());
    }

    @Override
    public long readLong() throws IOException {
        return Long.reverseBytes(this.stream.readLong());
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
        byte[] data = new byte[this.readUnsignedShort()];
        this.readFully(data);
        return new String(data, StandardCharsets.UTF_8);
    }

}
