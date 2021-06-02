package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTByte;

import java.io.IOException;

public class NBTByteWriter extends NBTWriter<NBTByte> {

    public NBTByteWriter(LittleEndianDataOutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTByte tag) throws IOException {
        this.stream.writeByte(tag.getValue());
    }

}
