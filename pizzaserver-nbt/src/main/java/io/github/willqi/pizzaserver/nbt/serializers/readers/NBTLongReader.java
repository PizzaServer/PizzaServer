package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.tags.NBTLong;

import java.io.IOException;
import java.io.InputStream;

public class NBTLongReader extends NBTReader<NBTLong> {

    public NBTLongReader(InputStream stream) {
        super(stream);
    }

    @Override
    protected NBTLong parse(String tagName) throws IOException {
        return new NBTLong(tagName, this.stream.readLong());
    }

}
