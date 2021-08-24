package io.github.willqi.pizzaserver.nbt.tags;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EqualityTest {

    @Test
    public void compoundsShouldMatch() {
        NBTList<Short> nbtList = new NBTList<>(NBTTag.SHORT_TAG_ID);
        nbtList.setContents(new Short[]{ 1, 2 });

        NBTCompound compound = new NBTCompound("name")
                .setByte("byte", (byte)0)
                .setByteArray("byte_array", new byte[]{ 0, 1, 2 })
                .setCompound("compound", new NBTCompound("inner_compound"))
                .setDouble("double", 12d)
                .setFloat("float", 2f)
                .setInteger("int", 12)
                .setList("list", nbtList)
                .setLong("long", 2L)
                .setLongArray("long_array", new long[]{ 1L, 2L, 3L })
                .setIntegerArray("int_list", new int[]{1, 2, 3})
                .setShort("short", (short)5)
                .setString("string","string content");

        NBTCompound otherCompound = new NBTCompound("name")
                .setByte("byte", (byte)0)
                .setByteArray("byte_array", new byte[]{ 0, 1, 2 })
                .setCompound("compound", new NBTCompound("inner_compound"))
                .setDouble("double", 12d)
                .setFloat("float", 2f)
                .setInteger("int", 12)
                .setList("list", nbtList)
                .setLong("long", 2L)
                .setLongArray("long_array", new long[]{ 1L, 2L, 3L })
                .setIntegerArray("int_list", new int[]{1, 2, 3})
                .setShort("short", (short)5)
                .setString("string","string content");

        assertEquals(compound, otherCompound);
    }

    @Test
    public void compoundsShouldNotMatch() {
        NBTCompound compound = new NBTCompound("name")
            .setInteger("test", 12)
            .setIntegerArray("list", new int[]{1, 2, 3});

        NBTCompound otherCompound = new NBTCompound("name")
                .setInteger("test", 123);

        assertNotEquals(compound, otherCompound);

    }

}
