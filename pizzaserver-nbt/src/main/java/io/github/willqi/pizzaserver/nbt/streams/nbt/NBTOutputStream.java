package io.github.willqi.pizzaserver.nbt.streams.nbt;

import io.github.willqi.pizzaserver.nbt.serializers.writers.*;
import io.github.willqi.pizzaserver.nbt.streams.ld.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.IOException;
import java.io.OutputStream;

public class NBTOutputStream extends OutputStream {

    private final LittleEndianDataOutputStream stream;

    private final NBTByteWriter byteWriter;
    private final NBTShortWriter shortWriter;
    private final NBTIntegerWriter integerWriter;
    private final NBTLongWriter longWriter;
    private final NBTFloatWriter floatWriter;
    private final NBTDoubleWriter doubleWriter;
    private final NBTByteArrayWriter byteArrayWriter;
    private final NBTStringWriter stringWriter;
    private final NBTListWriter<? extends NBTTag> listWriter;
    private final NBTCompoundWriter compoundWriter;
    private final NBTIntegerArrayWriter integerArrayWriter;

    public NBTOutputStream(OutputStream stream) {
        if (stream instanceof LittleEndianDataOutputStream) {
            this.stream = (LittleEndianDataOutputStream)stream;
        } else {
            this.stream = new LittleEndianDataOutputStream(stream);
        }

        this.byteWriter = new NBTByteWriter(this.stream);
        this.shortWriter = new NBTShortWriter(this.stream);
        this.integerWriter = new NBTIntegerWriter(this.stream);
        this.longWriter = new NBTLongWriter(this.stream);
        this.floatWriter = new NBTFloatWriter(this.stream);
        this.doubleWriter = new NBTDoubleWriter(this.stream);
        this.byteArrayWriter = new NBTByteArrayWriter(this.stream);
        this.stringWriter = new NBTStringWriter(this.stream);
        this.listWriter = new NBTListWriter<>(this.stream);
        this.compoundWriter = new NBTCompoundWriter(this.stream);
        this.integerArrayWriter = new NBTIntegerArrayWriter(this.stream);
    }

    @Override
    public void write(int b) throws IOException {
        this.stream.write(b);
    }

    public void writeByte(NBTByte nbtByte) throws IOException {
        this.byteWriter.write(nbtByte);
    }

    public void writeShort(NBTShort nbtShort) throws IOException {
        this.shortWriter.write(nbtShort);
    }

    public void writeInt(NBTInteger nbtInteger) throws IOException {
        this.integerWriter.write(nbtInteger);
    }

    public void writeLong(NBTLong nbtLong) throws IOException {
        this.longWriter.write(nbtLong);
    }

    public void writeFloat(NBTFloat nbtFloat) throws IOException {
        this.floatWriter.write(nbtFloat);
    }

    public void writeDouble(NBTDouble nbtDouble) throws IOException {
        this.doubleWriter.write(nbtDouble);
    }

    public void writeByteArray(NBTByteArray nbtByteArray) throws IOException {
        this.byteArrayWriter.write(nbtByteArray);
    }

    public void writeString(NBTString nbtString) throws IOException {
        this.stringWriter.write(nbtString);
    }

    public void writeList(NBTList nbtList) throws IOException {
        this.listWriter.write(nbtList);
    }

    public void writeCompound(NBTCompound compound) throws IOException {
        this.compoundWriter.write(compound);
    }

    public void writeIntegerArray(NBTIntegerArray integerArray) throws IOException {
        this.integerArrayWriter.write(integerArray);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
