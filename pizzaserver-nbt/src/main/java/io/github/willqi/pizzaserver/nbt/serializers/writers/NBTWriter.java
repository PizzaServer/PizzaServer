package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.serializers.utils.StreamUtility;
import io.github.willqi.pizzaserver.nbt.tags.NBTTag;

import java.io.IOException;
import java.io.OutputStream;

public abstract class NBTWriter<T extends NBTTag> {

    public void write(OutputStream stream, T tag) throws IOException {
        stream.write(tag.getId());
        StreamUtility.putTagName(tag.getName(), stream);
    }

}
