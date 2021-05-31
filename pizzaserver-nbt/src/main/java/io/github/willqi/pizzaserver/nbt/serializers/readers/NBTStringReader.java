package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.tags.NBTString;

import java.io.IOException;
import java.io.InputStream;

public class NBTStringReader extends NBTReader<NBTString> {

    public NBTStringReader(InputStream stream) {
        super(stream);
    }

    @Override
    protected NBTString parse(String tagName) throws IOException {
        return new NBTString(tagName, this.stream.readUTF());
    }

}
