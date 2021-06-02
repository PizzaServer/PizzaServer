package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.ld.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTFloat;

import java.io.IOException;

public class NBTFloatWriter extends NBTWriter<NBTFloat> {

    public NBTFloatWriter(LittleEndianDataOutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTFloat tag) throws IOException {
        this.stream.writeFloat(tag.getValue());
    }

}
