package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;

import java.io.IOException;

public class NBTLongReader extends NBTReader<Long> {

    public static final NBTReader<Long> INSTANCE = new NBTLongReader();


    @Override
    public Long read(LittleEndianDataInputStream stream) throws IOException {
        return stream.readLong();
    }

}
