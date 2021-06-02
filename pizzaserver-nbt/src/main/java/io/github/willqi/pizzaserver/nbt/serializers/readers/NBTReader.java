package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTTag;

import java.io.IOException;
import java.io.InputStream;

public abstract class NBTReader<T extends NBTTag> {

    protected final VarIntDataInputStream stream;


    public NBTReader(InputStream stream) {
        this.stream = new VarIntDataInputStream(stream);
    }

    public T read() throws IOException {
        String name = this.stream.readUTF();
        return this.parse(name);
    }

    protected abstract T parse(String tagName) throws IOException;

}
