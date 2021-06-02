package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.ld.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTDouble;

import java.io.IOException;

public class NBTDoubleWriter extends NBTWriter<NBTDouble> {

    public NBTDoubleWriter(LittleEndianDataOutputStream stream) {
        super(stream);
    }


    @Override
    protected void writeTagData(NBTDouble tag) throws IOException {
        this.stream.writeDouble(tag.getValue());
    }

}
