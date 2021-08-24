package io.github.willqi.pizzaserver.nbt.streams.nbt;

import io.github.willqi.pizzaserver.nbt.serializers.readers.*;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.IOException;
import java.io.InputStream;

public class NBTInputStream extends InputStream {

    private final LittleEndianDataInputStream stream;


    public NBTInputStream(InputStream stream) {
        if (stream instanceof LittleEndianDataInputStream) {
            this.stream = (LittleEndianDataInputStream)stream;
        } else {
            this.stream = new LittleEndianDataInputStream(stream);
        }
    }

    @Override
    public int available() throws IOException {
        return this.stream.available();
    }

    @Override
    public int read() throws IOException {
        return this.stream.read();
    }

    public byte readByte() throws IOException {
        this.ensureNbtId(NBTTag.BYTE_TAG_ID);
        return NBTByteReader.INSTANCE.read(this.stream);
    }

    public short readShort() throws IOException {
        this.ensureNbtId(NBTTag.SHORT_TAG_ID);
        return NBTShortReader.INSTANCE.read(this.stream);
    }

    public int readInteger() throws IOException {
        this.ensureNbtId(NBTTag.INT_TAG_ID);
        return NBTIntegerReader.INSTANCE.read(this.stream);
    }

    public long readLong() throws IOException {
        this.ensureNbtId(NBTTag.LONG_TAG_ID);
        return NBTLongReader.INSTANCE.read(this.stream);
    }

    public float readFloat() throws IOException {
        this.ensureNbtId(NBTTag.FLOAT_TAG_ID);
        return NBTFloatReader.INSTANCE.read(this.stream);
    }

    public double readDouble() throws IOException {
        this.ensureNbtId(NBTTag.DOUBLE_TAG_ID);
        return NBTDoubleReader.INSTANCE.read(this.stream);
    }

    public byte[] readByteArray() throws IOException {
        this.ensureNbtId(NBTTag.BYTE_ARRAY_TAG_ID);
        return NBTByteArrayReader.INSTANCE.read(this.stream);
    }

    public String readString() throws IOException {
        this.ensureNbtId(NBTTag.STRING_TAG_ID);
        return NBTStringReader.INSTANCE.read(this.stream);
    }

    public int[] readIntegerArray() throws IOException {
        this.ensureNbtId(NBTTag.INT_ARRAY_TAG_ID);
        return NBTIntegerArrayReader.INSTANCE.read(this.stream);
    }

    public long[] readLongArray() throws IOException {
        this.ensureNbtId(NBTTag.LONG_ARRAY_TAG_ID);
        return NBTLongArrayReader.INSTANCE.read(this.stream);
    }

    public NBTList readList() throws IOException {
        this.ensureNbtId(NBTTag.LIST_TAG_ID);
        return NBTListReader.INSTANCE.read(this.stream);
    }


    public NBTCompound readCompound() throws IOException {
        this.ensureNbtId(NBTTag.COMPOUND_TAG_ID);
        String name = this.stream.readUTF();
        NBTCompound compound = NBTCompoundReader.INSTANCE.read(this.stream);
        compound.setName(name);

        return compound;
    }

    private void ensureNbtId(int id) throws IOException {
        int nbtId = this.stream.read();
        if (nbtId != id) {
            throw new IOException("Attempted to read NBT type: " + id + " but found " + nbtId + " instead.");
        }
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }

}
