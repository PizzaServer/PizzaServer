package io.github.willqi.pizzaserver.nbt.streams.le;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Bedrock NBT writing stream for NBT written to the disk.
 */
public class LittleEndianDataOutputStream extends OutputStream implements DataOutput {

    protected final DataOutputStream stream;


    public LittleEndianDataOutputStream(OutputStream stream) {
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
    public void writeBoolean(boolean value) throws IOException {
        this.stream.writeBoolean(value);
    }

    @Override
    public void writeByte(int value) throws IOException {
        this.stream.writeByte(value);
    }

    @Override
    public void writeShort(int value) throws IOException {
        this.stream.writeShort(Short.reverseBytes((short) value));
    }

    @Override
    public void writeChar(int value) throws IOException {
        this.stream.writeChar(Character.reverseBytes((char) value));
    }

    @Override
    public void writeInt(int value) throws IOException {
        this.stream.writeInt(Integer.reverseBytes(value));
    }

    @Override
    public void writeLong(long value) throws IOException {
        this.stream.writeLong(Long.reverseBytes(value));
    }

    @Override
    public void writeFloat(float value) throws IOException {
        this.stream.writeInt(Integer.reverseBytes(Float.floatToIntBits(value)));
    }

    @Override
    public void writeDouble(double value) throws IOException {
        this.stream.writeLong(Long.reverseBytes(Double.doubleToLongBits(value)));
    }

    @Override
    public void writeBytes(String str) throws IOException {
        this.stream.writeBytes(str);
    }

    @Override
    public void writeChars(String str) throws IOException {
        this.stream.writeChars(str);
    }

    @Override
    public void writeUTF(String str) throws IOException {
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        this.writeShort(data.length);
        this.write(data);
    }

}
