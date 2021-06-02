package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTLong;

import java.io.IOException;

public class NBTLongWriter extends NBTWriter<NBTLong> {

    public NBTLongWriter(LittleEndianDataOutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTLong tag) throws IOException {
        this.stream.writeLong(tag.getValue());
    }

}
