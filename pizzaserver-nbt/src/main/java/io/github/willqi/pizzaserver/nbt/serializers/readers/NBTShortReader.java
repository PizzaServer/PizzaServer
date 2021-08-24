package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;

import java.io.IOException;

public class NBTShortReader extends NBTReader<Short> {

    public static final NBTReader<Short> INSTANCE = new NBTShortReader();


    @Override
    public Short read(LittleEndianDataInputStream stream) throws IOException {
        return stream.readShort();
    }

}
