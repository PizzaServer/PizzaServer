package io.github.willqi.pizzaserver.nbt.streams.nbt;

import io.github.willqi.pizzaserver.nbt.serializers.writers.*;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.IOException;
import java.io.OutputStream;

public class NBTOutputStream extends OutputStream {

    private final LittleEndianDataOutputStream stream;


    public NBTOutputStream(OutputStream stream) {
        if (stream instanceof LittleEndianDataOutputStream) {
            this.stream = (LittleEndianDataOutputStream)stream;
        } else {
            this.stream = new LittleEndianDataOutputStream(stream);
        }
    }

    @Override
    public void write(int b) throws IOException {
        this.stream.write(b);
    }

    public void writeByte(byte value) throws IOException {
        this.write(NBTTag.BYTE_TAG_ID);
        NBTByteWriter.INSTANCE.write(this.stream, value);
    }

    public void writeShort(short value) throws IOException {
        this.write(NBTTag.SHORT_TAG_ID);
        NBTShortWriter.INSTANCE.write(this.stream, value);
    }

    public void writeInt(int value) throws IOException {
        this.write(NBTTag.INT_TAG_ID);
        NBTIntegerWriter.INSTANCE.write(this.stream, value);
    }

    public void writeLong(long value) throws IOException {
        this.write(NBTTag.LONG_TAG_ID);
        NBTLongWriter.INSTANCE.write(this.stream, value);
    }

    public void writeFloat(float value) throws IOException {
        this.write(NBTTag.FLOAT_TAG_ID);
        NBTFloatWriter.INSTANCE.write(this.stream, value);
    }

    public void writeDouble(double value) throws IOException {
        this.write(NBTTag.DOUBLE_TAG_ID);
        NBTDoubleWriter.INSTANCE.write(this.stream, value);
    }

    public void writeByteArray(byte[] value) throws IOException {
        this.write(NBTTag.BYTE_ARRAY_TAG_ID);
        NBTByteArrayWriter.INSTANCE.write(this.stream, value);
    }

    public void writeString(String value) throws IOException {
        this.write(NBTTag.STRING_TAG_ID);
        NBTStringWriter.INSTANCE.write(this.stream, value);
    }

    public void writeList(NBTList value) throws IOException {
        this.write(NBTTag.LIST_TAG_ID);
        NBTListWriter.INSTANCE.write(this.stream, value);
    }

    public void writeCompound(NBTCompound value) throws IOException {
        this.write(NBTTag.COMPOUND_TAG_ID);
        this.stream.writeUTF(value.getName());
        NBTCompoundWriter.INSTANCE.write(this.stream, value);
    }

    public void writeIntegerArray(int[] value) throws IOException {
        this.write(NBTTag.INT_ARRAY_TAG_ID);
        NBTIntegerArrayWriter.INSTANCE.write(this.stream, value);
    }

    public void writeLongArray(long[] value) throws IOException {
        this.write(NBTTag.LONG_ARRAY_TAG_ID);
        NBTLongArrayWriter.INSTANCE.write(this.stream, value);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

}
