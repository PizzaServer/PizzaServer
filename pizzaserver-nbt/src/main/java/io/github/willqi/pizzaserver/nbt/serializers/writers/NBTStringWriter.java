package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTString;

import java.io.IOException;

public class NBTStringWriter extends NBTWriter<NBTString> {

    public NBTStringWriter(LittleEndianDataOutputStream stream) {
        super(stream);
    }

    @Override
    protected void writeTagData(NBTString tag) throws IOException {
        this.stream.writeUTF(tag.getValue());
    }

}
