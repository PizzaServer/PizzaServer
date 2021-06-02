package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.ld.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTShort;

import java.io.IOException;

public class NBTShortReader extends NBTReader<NBTShort> {

    public NBTShortReader(LittleEndianDataInputStream stream) {
        super(stream);
    }

    @Override
    protected NBTShort parse(String tagName) throws IOException {
        return new NBTShort(tagName, this.stream.readShort());
    }

}
