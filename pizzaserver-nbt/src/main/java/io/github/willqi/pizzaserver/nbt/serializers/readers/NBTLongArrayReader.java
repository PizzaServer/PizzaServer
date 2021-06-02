package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTLongArray;

import java.io.IOException;

public class NBTLongArrayReader extends NBTReader<NBTLongArray> {

    private final NBTLongReader longReader;


    public NBTLongArrayReader(LittleEndianDataInputStream stream) {
        super(stream);
        this.longReader = new NBTLongReader(this.stream);
    }

    @Override
    protected NBTLongArray parse(String tagName) throws IOException {
        int length = this.stream.readInt();
        long[] data = new long[length];
        for (int i = 0; i < data.length; i++) {
            data[i] = this.longReader.parse().getValue();
        }
        return new NBTLongArray(tagName, data);
    }

}
