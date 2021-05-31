package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.tags.NBTByteArray;

import java.io.IOException;
import java.io.InputStream;

public class NBTByteArrayReader extends NBTReader<NBTByteArray> {

    public NBTByteArrayReader(InputStream stream) {
        super(stream);
    }

    @Override
    protected NBTByteArray parse(String tagName) throws IOException {
        int elements = this.stream.readInt();
        byte[] data = new byte[elements];
        for (int i = 0; i < data.length; i++) {
            data[i] = this.stream.readByte();
        }
        return new NBTByteArray(tagName, data);
    }

}
