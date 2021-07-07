package io.github.willqi.pizzaserver.nbt.tags;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EqualityTest {

    @Test
    public void compoundsShouldMatch() {
        NBTList<NBTShort> nbtList = new NBTList<>(NBTShort.ID);
        nbtList.setContents(new NBTShort[]{ new NBTShort((short)1), new NBTShort((short)2) });

        NBTCompound compound = new NBTCompound("name");
        compound.put("byte", new NBTByte((byte)0));
        compound.put("byte_array", new NBTByteArray(new byte[]{ 0, 1, 2 }));
        compound.put("compound", new NBTCompound("inner_compound"));
        compound.put("double", new NBTDouble(12));
        compound.put("float", new NBTFloat(2f));
        compound.put("int", new NBTInteger(12));
        compound.put("int_list", new NBTIntegerArray(new int[]{1, 2, 3}));
        compound.put("list", nbtList);
        compound.put("long", new NBTLong(2L));
        compound.put("long_array", new NBTLongArray(new long[]{ 1L, 2L, 3L }));
        compound.put("short", new NBTShort((short)5));
        compound.put("string", new NBTString("string content"));

        NBTCompound otherCompound = new NBTCompound("name");
        otherCompound.put("byte", new NBTByte((byte)0));
        otherCompound.put("byte_array", new NBTByteArray(new byte[]{ 0, 1, 2 }));
        otherCompound.put("compound", new NBTCompound("inner_compound"));
        otherCompound.put("double", new NBTDouble(12));
        otherCompound.put("float", new NBTFloat(2f));
        otherCompound.put("int", new NBTInteger(12));
        otherCompound.put("int_list", new NBTIntegerArray(new int[]{1, 2, 3}));
        otherCompound.put("list", nbtList);
        otherCompound.put("long", new NBTLong(2L));
        otherCompound.put("long_array", new NBTLongArray(new long[]{ 1L, 2L, 3L }));
        otherCompound.put("short", new NBTShort((short)5));
        otherCompound.put("string", new NBTString("string content"));

        assertEquals(compound, otherCompound);
    }

    @Test
    public void compoundsShouldNotMatch() {
        NBTCompound compound = new NBTCompound("name");
        compound.put("test", new NBTInteger(12));
        compound.put("list", new NBTIntegerArray(new int[]{1, 2, 3}));

        NBTCompound otherCompound = new NBTCompound("name");
        otherCompound.put("test", new NBTInteger(12));

        assertNotEquals(compound, otherCompound);

    }

}
