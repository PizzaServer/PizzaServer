package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.tags.NBTFloat;

import java.io.IOException;
import java.io.InputStream;

public class NBTFloatReader extends NBTReader<NBTFloat> {

    public NBTFloatReader(InputStream stream) {
        super(stream);
    }

    @Override
    protected NBTFloat parse(String tagName) throws IOException {
        return new NBTFloat(tagName, this.stream.readFloat());
    }

}
