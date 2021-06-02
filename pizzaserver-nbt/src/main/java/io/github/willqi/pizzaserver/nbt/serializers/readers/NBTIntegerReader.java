package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTInteger;

import java.io.IOException;

public class NBTIntegerReader extends NBTReader<NBTInteger> {

    public NBTIntegerReader(LittleEndianDataInputStream stream) {
        super(stream);
    }

    @Override
    protected NBTInteger parse(String tagName) throws IOException {
        return new NBTInteger(tagName, this.stream.readInt());
    }

}
