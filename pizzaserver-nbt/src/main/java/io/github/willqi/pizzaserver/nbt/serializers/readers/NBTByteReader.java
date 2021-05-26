package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.serializers.utils.StreamUtility;
import io.github.willqi.pizzaserver.nbt.tags.NBTByte;

import java.io.IOException;
import java.io.InputStream;

public class NBTByteReader extends NBTReader<NBTByte> {

    public static final NBTByteReader INSTANCE = new NBTByteReader();


    private NBTByteReader() {}

    @Override
    public NBTByte read(InputStream stream) throws IOException {
        stream.skip(1);
        String tagName = StreamUtility.readName(stream);
        return new NBTByte(tagName, (byte)stream.read());
    }

}
