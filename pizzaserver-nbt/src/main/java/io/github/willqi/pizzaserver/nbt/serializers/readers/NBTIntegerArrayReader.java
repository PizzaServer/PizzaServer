package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTIntegerArray;

import java.io.IOException;

public class NBTIntegerArrayReader extends NBTReader<NBTIntegerArray> {

    private final NBTIntegerReader integerReader;


    public NBTIntegerArrayReader(LittleEndianDataInputStream stream) {
        super(stream);
        this.integerReader = new NBTIntegerReader(this.stream);
    }

    @Override
    protected NBTIntegerArray parse(String tagName) throws IOException {
        int length = this.stream.readInt();
        int[] data = new int[length];

        for (int i = 0; i < data.length; i++) {
            data[i] = this.integerReader.parse().getValue();
        }

        return new NBTIntegerArray(tagName, data);
    }

}
