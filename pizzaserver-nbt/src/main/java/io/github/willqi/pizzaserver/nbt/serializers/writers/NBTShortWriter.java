package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.tags.NBTShort;

import java.io.IOException;
import java.io.OutputStream;

public class NBTShortWriter extends NBTWriter<NBTShort> {

   public NBTShortWriter(OutputStream stream) {
       super(stream);
   }

    @Override
    protected void writeTagData(NBTShort tag) throws IOException {
        this.stream.writeShort(tag.getValue());
    }

}
