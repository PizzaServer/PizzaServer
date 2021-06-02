package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTIntegerArray;
import io.github.willqi.pizzaserver.nbt.tags.NBTInteger;

import java.io.IOException;

public class NBTIntegerArrayWriter extends NBTWriter<NBTIntegerArray> {

    private final NBTIntegerWriter integerWriter;


    public NBTIntegerArrayWriter(LittleEndianDataOutputStream stream) {
        super(stream);
        this.integerWriter = new NBTIntegerWriter(this.stream);
    }

    @Override
    protected void writeTagData(NBTIntegerArray tag) throws IOException {
        this.stream.writeInt(tag.getData().length);
        for (int integer : tag.getData()) {
            integerWriter.writeTagData(new NBTInteger(integer));
        }
    }
}
