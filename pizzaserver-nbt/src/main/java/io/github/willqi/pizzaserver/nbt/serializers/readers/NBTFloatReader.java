package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;

import java.io.IOException;

public class NBTFloatReader extends NBTReader<Float> {

    public static final NBTReader<Float> INSTANCE = new NBTFloatReader();


    @Override
    public Float read(LittleEndianDataInputStream stream) throws IOException {
        return stream.readFloat();
    }

}
