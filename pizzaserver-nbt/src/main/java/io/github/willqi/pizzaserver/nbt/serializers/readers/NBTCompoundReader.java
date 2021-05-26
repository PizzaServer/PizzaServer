package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.io.IOException;
import java.io.InputStream;

public class NBTCompoundReader extends NBTReader<NBTCompound> {

    public static final NBTCompoundReader INSTANCE = new NBTCompoundReader();


    private NBTCompoundReader() {}

    @Override
    public NBTCompound read(InputStream stream) throws IOException {
        return null;
    }

}
