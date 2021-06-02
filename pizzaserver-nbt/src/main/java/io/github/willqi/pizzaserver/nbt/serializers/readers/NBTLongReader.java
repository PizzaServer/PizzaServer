package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTLong;

import java.io.IOException;

public class NBTLongReader extends NBTReader<NBTLong> {

    public NBTLongReader(LittleEndianDataInputStream stream) {
        super(stream);
    }

    @Override
    protected NBTLong parse(String tagName) throws IOException {
        return new NBTLong(tagName, this.stream.readLong());
    }

}
