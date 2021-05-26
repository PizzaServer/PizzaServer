package io.github.willqi.pizzaserver.nbt.serializers.writers;

import io.github.willqi.pizzaserver.nbt.serializers.utils.StreamUtility;
import io.github.willqi.pizzaserver.nbt.tags.NBTByte;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.nbt.tags.NBTTag;

import java.io.IOException;
import java.io.OutputStream;

public class NBTCompoundWriter extends NBTWriter<NBTCompound> {

    public static final NBTCompoundWriter INSTANCE = new NBTCompoundWriter();


    private NBTCompoundWriter() {}

    @Override
    public void write(OutputStream stream, NBTCompound tag) throws IOException {
        writeContents(stream, tag);
        stream.write(NBTTag.END_ID);
    }

    private static void writeContents(OutputStream stream, NBTCompound tag) throws IOException {
        for (String name : tag.keySet()) {

            NBTTag childTag = tag.get(name);
            switch (childTag.getId()) {
                case NBTByte.ID:
                    NBTByteWriter.INSTANCE.write(stream, (NBTByte)childTag);
                    break;
                case NBTCompound.ID:
                    INSTANCE.write(stream, tag);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported/invalid NBT tag id found when writing contents to NBTCompoundWriter. Id: " + tag.getId());
            }

        }
    }

}
