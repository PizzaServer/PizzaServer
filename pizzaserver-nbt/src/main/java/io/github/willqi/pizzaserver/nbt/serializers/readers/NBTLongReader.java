package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.serializers.utils.StreamUtility;
import io.github.willqi.pizzaserver.nbt.tags.NBTLong;

import java.io.IOException;
import java.io.InputStream;

public class NBTLongReader extends NBTReader<NBTLong> {

    public static final NBTLongReader INSTANCE = new NBTLongReader();


    private NBTLongReader() {}

    @Override
    public NBTLong read(InputStream stream) throws IOException {
        stream.skip(1);
        String tagName = StreamUtility.readName(stream);
        return new NBTLong(tagName, StreamUtility.readLong(stream));
    }

}
