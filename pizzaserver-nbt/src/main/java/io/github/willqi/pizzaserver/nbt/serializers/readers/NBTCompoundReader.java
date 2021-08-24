package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.nbt.tags.NBTTag;

import java.io.IOException;

public class NBTCompoundReader extends NBTReader<NBTCompound> {

    public static final NBTReader<NBTCompound> INSTANCE = new NBTCompoundReader();


    @Override
    public NBTCompound read(LittleEndianDataInputStream stream) throws IOException {
        NBTCompound compound = new NBTCompound();

        boolean reachedEnd = false; // if we reach this compound's end tag
        while (stream.available() > 0) {
            int nbtTagId = stream.readByte();
            String tagName;
            switch (nbtTagId) {
                case NBTTag.BYTE_TAG_ID:
                    tagName = stream.readUTF();
                    compound.setByte(tagName, NBTByteReader.INSTANCE.read(stream));
                    break;
                case NBTTag.SHORT_TAG_ID:
                    tagName = stream.readUTF();
                    compound.setShort(tagName, NBTShortReader.INSTANCE.read(stream));
                    break;
                case NBTTag.INT_TAG_ID:
                    tagName = stream.readUTF();
                    compound.setInteger(tagName, NBTIntegerReader.INSTANCE.read(stream));
                    break;
                case NBTTag.LONG_TAG_ID:
                    tagName = stream.readUTF();
                    compound.setLong(tagName, NBTLongReader.INSTANCE.read(stream));
                    break;
                case NBTTag.FLOAT_TAG_ID:
                    tagName = stream.readUTF();
                    compound.setFloat(tagName, NBTFloatReader.INSTANCE.read(stream));
                    break;
                case NBTTag.DOUBLE_TAG_ID:
                    tagName = stream.readUTF();
                    compound.setDouble(tagName, NBTDoubleReader.INSTANCE.read(stream));
                    break;
                case NBTTag.BYTE_ARRAY_TAG_ID:
                    tagName = stream.readUTF();
                    compound.setByteArray(tagName, NBTByteArrayReader.INSTANCE.read(stream));
                    break;
                case NBTTag.STRING_TAG_ID:
                    tagName = stream.readUTF();
                    compound.setString(tagName, NBTStringReader.INSTANCE.read(stream));
                    break;
                case NBTTag.LIST_TAG_ID:
                    tagName = stream.readUTF();
                    compound.setList(tagName, NBTListReader.INSTANCE.read(stream));
                    break;
                case NBTTag.COMPOUND_TAG_ID:
                    tagName = stream.readUTF();
                    compound.setCompound(tagName, NBTCompoundReader.INSTANCE.read(stream));
                    break;
                case NBTTag.INT_ARRAY_TAG_ID:
                    tagName = stream.readUTF();
                    compound.setIntegerArray(tagName, NBTIntegerArrayReader.INSTANCE.read(stream));
                    break;
                case NBTTag.LONG_ARRAY_TAG_ID:
                    tagName = stream.readUTF();
                    compound.setLongArray(tagName, NBTLongArrayReader.INSTANCE.read(stream));
                    break;
                case NBTTag.END_TAG_ID:
                    reachedEnd = true;
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown NBT tag id found: " + nbtTagId);
            }

            if (reachedEnd) {
                // Reached the end tag
                break;
            }
        }
        return compound;
    }

}
