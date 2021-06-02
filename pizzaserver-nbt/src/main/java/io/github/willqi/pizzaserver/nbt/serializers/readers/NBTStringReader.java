package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.ld.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTString;

import java.io.IOException;

public class NBTStringReader extends NBTReader<NBTString> {

    public NBTStringReader(LittleEndianDataInputStream stream) {
        super(stream);
    }

    @Override
    protected NBTString parse(String tagName) throws IOException {
        return new NBTString(tagName, this.stream.readUTF());
    }

}
