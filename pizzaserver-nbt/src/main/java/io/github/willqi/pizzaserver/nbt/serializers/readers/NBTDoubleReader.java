package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTDouble;

import java.io.IOException;

public class NBTDoubleReader extends NBTReader<NBTDouble> {

    public NBTDoubleReader(LittleEndianDataInputStream stream) {
        super(stream);
    }

    @Override
    protected NBTDouble parse(String tagName) throws IOException {
        return new NBTDouble(tagName, this.stream.readDouble());
    }

}
