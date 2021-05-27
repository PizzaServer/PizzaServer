package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.serializers.utils.StreamUtility;
import io.github.willqi.pizzaserver.nbt.tags.NBTShort;

import java.io.IOException;
import java.io.InputStream;

public class NBTShortReader extends NBTReader<NBTShort> {

    public static final NBTShortReader INSTANCE = new NBTShortReader();


    private NBTShortReader() {}

    @Override
    public NBTShort read(InputStream stream) throws IOException {
        stream.skip(1);
        String tagName = StreamUtility.readName(stream);
        return new NBTShort(tagName, StreamUtility.readShort(stream));
    }

}
