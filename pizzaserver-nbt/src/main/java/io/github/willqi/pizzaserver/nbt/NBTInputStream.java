package io.github.willqi.pizzaserver.nbt;

import io.github.willqi.pizzaserver.nbt.serializers.readers.NBTByteReader;
import io.github.willqi.pizzaserver.nbt.serializers.readers.NBTCompoundReader;
import io.github.willqi.pizzaserver.nbt.tags.NBTByte;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.io.IOException;
import java.io.InputStream;

public class NBTInputStream extends InputStream {

    private final InputStream stream;

    public NBTInputStream(InputStream stream) {
        this.stream = stream;
    }

    @Override
    public int read() throws IOException {
        return this.stream.read();
    }

    public NBTCompound readCompound() throws IOException {
        return NBTCompoundReader.INSTANCE.read(this.stream);
    }

    public NBTByte readByte() throws IOException {
        return NBTByteReader.INSTANCE.read(this.stream);
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }

}
