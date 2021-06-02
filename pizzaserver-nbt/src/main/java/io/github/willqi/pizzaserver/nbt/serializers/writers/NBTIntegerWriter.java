package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTInteger;

import java.io.IOException;

public class NBTIntegerWriter extends NBTWriter<NBTInteger> {

    public NBTIntegerWriter(LittleEndianDataOutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTInteger tag) throws IOException {
        this.stream.writeInt(tag.getValue());
    }

}
