package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.tags.NBTTag;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class NBTReader<T extends NBTTag> {

    protected final DataInputStream stream;


    public NBTReader(InputStream stream) {
        this.stream = new DataInputStream(stream);
    }

    public T read() throws IOException {
        this.stream.skipBytes(1);   // Tag id
        String name = this.stream.readUTF();
        return this.parse(name);
    }

    protected abstract T parse(String tagName) throws IOException;

}
