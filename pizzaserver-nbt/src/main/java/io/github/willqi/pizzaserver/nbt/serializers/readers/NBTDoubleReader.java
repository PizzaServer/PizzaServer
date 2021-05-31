package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.tags.NBTDouble;

import java.io.IOException;
import java.io.InputStream;

public class NBTDoubleReader extends NBTReader<NBTDouble> {

    public NBTDoubleReader(InputStream stream) {
        super(stream);
    }

    @Override
    protected NBTDouble parse(String tagName) throws IOException {
        return new NBTDouble(tagName, this.stream.readDouble());
    }

}
