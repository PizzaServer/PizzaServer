package io.github.willqi.pizzaserver.nbt;

import io.github.willqi.pizzaserver.nbt.serializers.writers.*;
import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NBTOutputStream extends OutputStream {

    private final ByteArrayOutputStream stream = new ByteArrayOutputStream();

    private final NBTByteWriter byteWriter = new NBTByteWriter(this.stream);
    private final NBTShortWriter shortWriter = new NBTShortWriter(this.stream);
    private final NBTIntegerWriter integerWriter = new NBTIntegerWriter(this.stream);
    private final NBTLongWriter longWriter = new NBTLongWriter(this.stream);
    private final NBTFloatWriter floatWriter = new NBTFloatWriter(this.stream);
    private final NBTDoubleWriter doubleWriter = new NBTDoubleWriter(this.stream);
    private final NBTCompoundWriter compoundWriter = new NBTCompoundWriter(this.stream);

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

    public void writeCompound(NBTCompound compound) throws IOException {
        this.compoundWriter.write(compound);
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }

}
