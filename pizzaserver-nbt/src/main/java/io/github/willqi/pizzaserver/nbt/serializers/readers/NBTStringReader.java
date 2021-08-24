package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;

import java.io.IOException;

public class NBTStringReader extends NBTReader<String> {

    public static final NBTReader<String> INSTANCE = new NBTStringReader();


    @Override
    public String read(LittleEndianDataInputStream stream) throws IOException {
        return stream.readUTF();
    }

}
