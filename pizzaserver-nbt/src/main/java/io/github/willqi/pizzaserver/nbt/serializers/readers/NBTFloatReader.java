package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.serializers.utils.StreamUtility;
import io.github.willqi.pizzaserver.nbt.tags.NBTFloat;

import java.io.IOException;
import java.io.InputStream;

public class NBTFloatReader extends NBTReader<NBTFloat> {

    public static final NBTFloatReader INSTANCE = new NBTFloatReader();


    private NBTFloatReader() {}

    @Override
    public NBTFloat read(InputStream stream) throws IOException {
        stream.skip(1);
        String tagName = StreamUtility.readName(stream);
        return new NBTFloat(tagName, StreamUtility.readFloat(stream));
    }

}
