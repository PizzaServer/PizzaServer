package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.streams.ld.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTShort;

import java.io.IOException;

public class NBTShortWriter extends NBTWriter<NBTShort> {

   public NBTShortWriter(LittleEndianDataOutputStream stream) {
       super(stream);
   }

    @Override
    protected void writeTagData(NBTShort tag) throws IOException {
        this.stream.writeShort(tag.getValue());
    }

}
