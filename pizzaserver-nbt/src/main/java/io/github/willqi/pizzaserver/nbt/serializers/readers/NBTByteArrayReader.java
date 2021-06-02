package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.ld.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTByteArray;

import java.io.IOException;

public class NBTByteArrayReader extends NBTReader<NBTByteArray> {

    private final NBTByteReader byteReader;


    public NBTByteArrayReader(LittleEndianDataInputStream stream) {
        super(stream);
        this.byteReader = new NBTByteReader(this.stream);
    }

    @Override
    protected NBTByteArray parse(String tagName) throws IOException {
        int elements = this.stream.readInt();
        byte[] data = new byte[elements];
        for (int i = 0; i < data.length; i++) {
            data[i] = this.byteReader.parse().getValue();
        }
        return new NBTByteArray(tagName, data);
    }

}
