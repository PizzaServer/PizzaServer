package io.github.willqi.pizzaserver.nbt.serializers.readers;

import io.github.willqi.pizzaserver.nbt.streams.ld.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.*;

import java.io.IOException;

public class NBTCompoundReader extends NBTReader<NBTCompound> {

    private final NBTByteReader byteReader = new NBTByteReader(this.stream);
    private final NBTShortReader shortReader = new NBTShortReader(this.stream);
    private final NBTIntegerReader integerReader = new NBTIntegerReader(this.stream);
    private final NBTLongReader longReader = new NBTLongReader(this.stream);
    private final NBTFloatReader floatReader = new NBTFloatReader(this.stream);
    private final NBTDoubleReader doubleReader = new NBTDoubleReader(this.stream);
    private final NBTByteArrayReader byteArrayReader = new NBTByteArrayReader(this.stream);
    private final NBTStringReader stringReader = new NBTStringReader(this.stream);
    private final NBTIntegerArrayReader integerArrayReader = new NBTIntegerArrayReader(this.stream);


    public NBTCompoundReader(LittleEndianDataInputStream stream) {
        super(stream);
    }

    @Override
    protected NBTCompound parse(String tagName) throws IOException {
        NBTCompound compound = new NBTCompound(tagName);

        boolean reachedEnd = false; // if we reach this compound's end tag
        while (this.stream.available() > 0) {
            int nbtTagId = this.stream.read();

            switch (nbtTagId) {
                case NBTByte.ID:
                    NBTByte nbtByte = this.byteReader.read();
                    compound.put(nbtByte.getName(), nbtByte);
                    break;
                case NBTShort.ID:
                    NBTShort nbtShort = this.shortReader.read();
                    compound.put(nbtShort.getName(), nbtShort);
                    break;
                case NBTInteger.ID:
                    NBTInteger nbtInteger = this.integerReader.read();
                    compound.put(nbtInteger.getName(), nbtInteger);
                    break;
                case NBTLong.ID:
                    NBTLong nbtLong = this.longReader.read();
                    compound.put(nbtLong.getName(), nbtLong);
                    break;
                case NBTFloat.ID:
                    NBTFloat nbtFloat = this.floatReader.read();
                    compound.put(nbtFloat.getName(), nbtFloat);
                    break;
                case NBTDouble.ID:
                    NBTDouble nbtDouble = this.doubleReader.read();
                    compound.put(nbtDouble.getName(), nbtDouble);
                    break;
                case NBTByteArray.ID:
                    NBTByteArray nbtByteArray = this.byteArrayReader.read();
                    compound.put(nbtByteArray.getName(), nbtByteArray);
                    break;
                case NBTString.ID:
                    NBTString nbtString = this.stringReader.read();
                    compound.put(nbtString.getName(), nbtString);
                    break;
                case NBTList.ID:
                    NBTList<? extends NBTTag> nbtList = new NBTListReader<>(this.stream).read();
                    compound.put(nbtList.getName(), nbtList);
                    break;
                case NBTCompound.ID:
                    NBTCompound nbtCompound = new NBTCompoundReader(this.stream).read();
                    compound.put(nbtCompound.getName(), nbtCompound);
                    break;
                case NBTContainer.END_ID:
                    reachedEnd = true;
                    break;
                case NBTIntegerArray.ID:
                    NBTIntegerArray nbtIntegerArray = this.integerArrayReader.read();
                    compound.put(nbtIntegerArray.getName(), nbtIntegerArray);
                    break;
            }

            if (reachedEnd) {
                // Reached the end tag
                break;
            }
        }
        return compound;
    }

}
