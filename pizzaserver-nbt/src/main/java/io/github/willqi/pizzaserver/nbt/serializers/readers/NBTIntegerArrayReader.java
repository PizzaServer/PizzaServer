package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;

import java.io.IOException;

public class NBTIntegerArrayReader extends NBTReader<int[]> {

    public static final NBTReader<int[]> INSTANCE = new NBTIntegerArrayReader();


    @Override
    public int[] read(LittleEndianDataInputStream stream) throws IOException {
        int length = stream.readInt();
        int[] data = new int[length];

        for (int i = 0; i < data.length; i++) {
            data[i] = NBTIntegerReader.INSTANCE.read(stream);
        }

        return data;
    }

}
