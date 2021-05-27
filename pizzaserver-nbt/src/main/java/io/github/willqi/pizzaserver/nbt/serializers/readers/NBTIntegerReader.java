package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.serializers.utils.StreamUtility;
import io.github.willqi.pizzaserver.nbt.tags.NBTInteger;

import java.io.IOException;
import java.io.InputStream;

public class NBTIntegerReader extends NBTReader<NBTInteger> {

    public static final NBTIntegerReader INSTANCE = new NBTIntegerReader();


    private NBTIntegerReader() {}

    @Override
    public NBTInteger read(InputStream stream) throws IOException {
        stream.skip(1);
        String tagName = StreamUtility.readName(stream);
        return new NBTInteger(tagName, StreamUtility.readSignedInt(stream));
    }

}
