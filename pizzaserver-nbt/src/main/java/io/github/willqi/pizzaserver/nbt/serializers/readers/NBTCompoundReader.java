package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.io.IOException;
import java.io.InputStream;

public class NBTCompoundReader extends NBTReader<NBTCompound> {

    public NBTCompoundReader(InputStream stream) {
        super(stream);
    }

    @Override
    protected NBTCompound parse(String tagName) throws IOException {
        return null;
    }

}
