package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.IOException;

public class NBTListReader<T> extends NBTReader<NBTList<T>> {

    public static final NBTReader<NBTList> INSTANCE = new NBTListReader();


    @Override
    public NBTList<T> read(LittleEndianDataInputStream stream) throws IOException {
        int nbtId = stream.readByte();
        NBTList<T> list = new NBTList<T>(nbtId);

        Object[] contents;
        int length = stream.readInt();

        switch (nbtId) {
            case NBTTag.BYTE_TAG_ID:
                contents = new Byte[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = NBTByteReader.INSTANCE.read(stream);
                }
                break;
            case NBTTag.SHORT_TAG_ID:
                contents = new Short[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = NBTShortReader.INSTANCE.read(stream);
                }
                break;
            case NBTTag.INT_TAG_ID:
                contents = new Integer[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = NBTIntegerReader.INSTANCE.read(stream);
                }
                break;
            case NBTTag.LONG_TAG_ID:
                contents = new Long[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = NBTLongReader.INSTANCE.read(stream);
                }
                break;
            case NBTTag.FLOAT_TAG_ID:
                contents = new Float[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = NBTFloatReader.INSTANCE.read(stream);
                }
                break;
            case NBTTag.DOUBLE_TAG_ID:
                contents = new Double[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = NBTDoubleReader.INSTANCE.read(stream);
                }
                break;
            case NBTTag.BYTE_ARRAY_TAG_ID:
                contents = new byte[length][];
                for (int i = 0; i < length; i++) {
                    contents[i] = NBTByteArrayReader.INSTANCE.read(stream);
                }
                break;
            case NBTTag.STRING_TAG_ID:
                contents = new String[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = NBTStringReader.INSTANCE.read(stream);
                }
                break;
            case NBTTag.LIST_TAG_ID:
                contents = new NBTList[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = this.read(stream);
                }
                break;
            case NBTTag.COMPOUND_TAG_ID:
                contents = new NBTCompound[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = NBTCompoundReader.INSTANCE.read(stream);
                }
                break;
            case NBTTag.INT_ARRAY_TAG_ID:
                contents = new int[length][];
                for (int i = 0; i < length; i++) {
                    contents[i] = NBTIntegerArrayReader.INSTANCE.read(stream);
                }
                break;
            case NBTTag.LONG_ARRAY_TAG_ID:
                contents = new long[length][];
                for (int i = 0; i < length; i++) {
                    contents[i] = NBTLongArrayReader.INSTANCE.read(stream);
                }
                break;
            case NBTTag.END_TAG_ID:
                contents = new NBTTag[0];
                break;
            default:
                throw new UnsupportedOperationException("Unsupported/invalid NBT tag id found when reading contents in NBTListReader. Id: " + nbtId);
        }

        list.setContents((T[]) contents);
        return list;
    }

}
