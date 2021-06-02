package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.tags.NBTByte;

import java.io.IOException;
import java.io.InputStream;

public class NBTByteReader extends NBTReader<NBTByte> {
    
    public NBTByteReader(InputStream stream) {
        super(stream);
    }

    @Override
    public NBTByte parse(String tagName) throws IOException {
        return new NBTByte(tagName, this.stream.readByte());
    }

}
