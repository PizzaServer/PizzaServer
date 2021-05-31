package io.github.willqi.pizzaserver.nbt;

import io.github.willqi.pizzaserver.nbt.serializers.readers.*;
import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.IOException;
import java.io.InputStream;

public class NBTInputStream extends InputStream {

    private final InputStream stream;

    private final NBTByteReader byteReader;
    private final NBTShortReader shortReader;
    private final NBTIntegerReader integerReader;
    private final NBTLongReader longReader;
    private final NBTFloatReader floatReader;
    private final NBTDoubleReader doubleReader;
    private final NBTByteArrayReader byteArrayReader;
    private final NBTCompoundReader compoundReader;


    public NBTInputStream(InputStream stream) {
        this.stream = stream;
        this.byteReader = new NBTByteReader(this.stream);
        this.shortReader = new NBTShortReader(this.stream);
        this.integerReader = new NBTIntegerReader(this.stream);
        this.longReader = new NBTLongReader(this.stream);
        this.floatReader = new NBTFloatReader(this.stream);
        this.doubleReader = new NBTDoubleReader(this.stream);
        this.byteArrayReader = new NBTByteArrayReader(this.stream);
        this.compoundReader = new NBTCompoundReader(this.stream);
    }

    @Override
    public int read() throws IOException {
        return this.stream.read();
    }

    public NBTByte readByte() throws IOException {
        this.ensureNbtId(NBTByte.ID);
        return this.byteReader.read();
    }

    public NBTShort readShort() throws IOException {
        this.ensureNbtId(NBTShort.ID);
        return this.shortReader.read();
    }

    public NBTInteger readInteger() throws IOException {
        this.ensureNbtId(NBTInteger.ID);
        return this.integerReader.read();
    }

    public NBTLong readLong() throws IOException {
        this.ensureNbtId(NBTLong.ID);
        return this.longReader.read();
    }

    public NBTFloat readFloat() throws IOException {
        this.ensureNbtId(NBTFloat.ID);
        return this.floatReader.read();
    }

    public NBTDouble readDouble() throws IOException {
        this.ensureNbtId(NBTDouble.ID);
        return this.doubleReader.read();
    }

    public NBTByteArray readByteArray() throws IOException {
        this.ensureNbtId(NBTByteArray.ID);
        return this.byteArrayReader.read();
    }

    public NBTCompound readCompound() throws IOException {
        this.ensureNbtId(NBTCompound.ID);
        return this.compoundReader.read();
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
