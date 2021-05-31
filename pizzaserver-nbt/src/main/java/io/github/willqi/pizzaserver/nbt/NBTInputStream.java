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
    private final NBTCompoundReader compoundReader;


    public NBTInputStream(InputStream stream) {
        this.stream = stream;
        this.byteReader = new NBTByteReader(this.stream);
        this.shortReader = new NBTShortReader(this.stream);
        this.integerReader = new NBTIntegerReader(this.stream);
        this.longReader = new NBTLongReader(this.stream);
        this.floatReader = new NBTFloatReader(this.stream);
        this.doubleReader = new NBTDoubleReader(this.stream);
        this.compoundReader = new NBTCompoundReader(this.stream);
    }

    @Override
    public int read() throws IOException {
        return this.stream.read();
    }

    public NBTByte readByte() throws IOException {
        return this.byteReader.read();
    }

    public NBTShort readShort() throws IOException {
        return this.shortReader.read();
    }

    public NBTInteger readInteger() throws IOException {
        return this.integerReader.read();
    }

    public NBTLong readLong() throws IOException {
        return this.longReader.read();
    }

    public NBTFloat readFloat() throws IOException {
        return this.floatReader.read();
    }

    public NBTDouble readDouble() throws IOException {
        return this.doubleReader.read();
    }

    public NBTCompound readCompound() throws IOException {
        return this.compoundReader.read();
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }

}
