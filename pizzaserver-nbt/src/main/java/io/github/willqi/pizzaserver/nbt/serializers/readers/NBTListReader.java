package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.IOException;

public class NBTListReader<T extends NBTTag> extends NBTReader<NBTList<T>> {

    private final NBTByteReader byteReader = new NBTByteReader(this.stream);
    private final NBTShortReader shortReader = new NBTShortReader(this.stream);
    private final NBTIntegerReader integerReader = new NBTIntegerReader(this.stream);
    private final NBTLongReader longReader = new NBTLongReader(this.stream);
    private final NBTFloatReader floatReader = new NBTFloatReader(this.stream);
    private final NBTDoubleReader doubleReader = new NBTDoubleReader(this.stream);
    private final NBTByteArrayReader byteArrayReader = new NBTByteArrayReader(this.stream);
    private final NBTStringReader stringReader = new NBTStringReader(this.stream);
    private final NBTIntegerArrayReader integerArrayReader = new NBTIntegerArrayReader(this.stream);

    public NBTListReader(LittleEndianDataInputStream stream) {
        super(stream);
    }

    @Override
    protected NBTList<T> parse(String tagName) throws IOException {
        NBTList<T> list = new NBTList<>();
        int nbtId = this.stream.readByte();

        NBTTag[] contents;
        int length = this.stream.readInt();

        switch (nbtId) {
            case NBTByte.ID:
                contents = new NBTByte[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = this.byteReader.parse();
                }
                break;
            case NBTShort.ID:
                contents = new NBTShort[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = this.shortReader.parse();
                }
                break;
            case NBTInteger.ID:
                contents = new NBTInteger[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = this.integerReader.parse();
                }
                break;
            case NBTLong.ID:
                contents = new NBTLong[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = this.longReader.parse();
                }
                break;
            case NBTFloat.ID:
                contents = new NBTFloat[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = this.floatReader.parse();
                }
                break;
            case NBTDouble.ID:
                contents = new NBTDouble[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = this.doubleReader.parse();
                }
                break;
            case NBTByteArray.ID:
                contents = new NBTByteArray[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = this.byteArrayReader.parse();
                }
                break;
            case NBTString.ID:
                contents = new NBTString[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = this.stringReader.parse();
                }
                break;
            case NBTList.ID:
                contents = new NBTList[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = this.parse();
                }
                break;
            case NBTCompound.ID:
                contents = new NBTCompound[length];
                NBTCompoundReader compoundReader = new NBTCompoundReader(this.stream);
                for (int i = 0; i < length; i++) {
                    contents[i] = compoundReader.parse();
                }
                break;
            case NBTIntegerArray.ID:
                contents = new NBTIntegerArray[length];
                for (int i = 0; i < length; i++) {
                    contents[i] = this.integerArrayReader.parse();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported/invalid NBT tag id found when reading contents in NBTListReader. Id: " + nbtId);
        }

        list.setContents((T[])contents);

        return list;
    }

}
