package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;

import java.io.IOException;

public class NBTByteReader extends NBTReader<Byte> {

    public static final NBTReader<Byte> INSTANCE = new NBTByteReader();

    @Override
    public Byte read(LittleEndianDataInputStream stream) throws IOException {
        return stream.readByte();
    }

}
