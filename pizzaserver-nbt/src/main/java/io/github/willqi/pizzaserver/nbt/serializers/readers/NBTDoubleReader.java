package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;

import java.io.IOException;

public class NBTDoubleReader extends NBTReader<Double> {

    public static final NBTReader<Double> INSTANCE = new NBTDoubleReader();


    @Override
    public Double read(LittleEndianDataInputStream stream) throws IOException {
        return stream.readDouble();
    }

}
