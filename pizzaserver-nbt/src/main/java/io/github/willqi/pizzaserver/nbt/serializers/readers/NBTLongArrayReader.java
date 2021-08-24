package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;

import java.io.IOException;

public class NBTLongArrayReader extends NBTReader<long[]> {

    public static final NBTReader<long[]> INSTANCE = new NBTLongArrayReader();


    @Override
    public long[] read(LittleEndianDataInputStream stream) throws IOException {
        int length = stream.readInt();
        long[] data = new long[length];
        for (int i = 0; i < data.length; i++) {
            data[i] = NBTLongReader.INSTANCE.read(stream);
        }
        return data;
    }

}
