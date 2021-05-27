package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.serializers.utils.StreamUtility;
import io.github.willqi.pizzaserver.nbt.tags.NBTDouble;

import java.io.IOException;
import java.io.InputStream;

public class NBTDoubleReader extends NBTReader<NBTDouble> {

    public static final NBTDoubleReader INSTANCE = new NBTDoubleReader();


    private NBTDoubleReader() {}

    @Override
    public NBTDouble read(InputStream stream) throws IOException {
        stream.skip(1);
        String tagName = StreamUtility.readName(stream);
        return new NBTDouble(tagName, StreamUtility.readDouble(stream));
    }

}
