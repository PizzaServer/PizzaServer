package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;

import java.io.IOException;

public class NBTByteArrayReader extends NBTReader<byte[]> {

    public static final NBTReader<byte[]> INSTANCE = new NBTByteArrayReader();


    @Override
    public byte[] read(LittleEndianDataInputStream stream) throws IOException {
        int elements = stream.readInt();
        byte[] data = new byte[elements];
        for (int i = 0; i < data.length; i++) {
            data[i] = NBTByteReader.INSTANCE.read(stream);
        }
        return data;
    }

}
