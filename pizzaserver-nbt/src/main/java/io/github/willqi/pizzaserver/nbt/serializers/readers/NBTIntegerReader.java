package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;

import java.io.IOException;

public class NBTIntegerReader extends NBTReader<Integer> {

    public static final NBTReader<Integer> INSTANCE = new NBTIntegerReader();


    @Override
    public Integer read(LittleEndianDataInputStream stream) throws IOException {
        return stream.readInt();
    }

}
