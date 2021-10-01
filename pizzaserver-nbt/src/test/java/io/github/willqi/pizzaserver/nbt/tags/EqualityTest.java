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
                .putByte("byte", (byte) 0)
                .putByteArray("byte_array", new byte[]{ 0, 1, 2 })
                .putCompound("compound", new NBTCompound("inner_compound"))
                .putDouble("double", 12d)
                .putFloat("float", 2f)
                .putInteger("int", 12)
                .putList("list", nbtList)
                .putLong("long", 2L)
                .putLongArray("long_array", new long[]{ 1L, 2L, 3L })
                .putIntegerArray("int_list", new int[]{1, 2, 3})
                .putShort("short", (short) 5)
                .putString("string", "string content");

        NBTCompound otherCompound = new NBTCompound("name")
                .putByte("byte", (byte) 0)
                .putByteArray("byte_array", new byte[]{ 0, 1, 2 })
                .putCompound("compound", new NBTCompound("inner_compound"))
                .putDouble("double", 12d)
                .putFloat("float", 2f)
                .putInteger("int", 12)
                .putList("list", nbtList)
                .putLong("long", 2L)
                .putLongArray("long_array", new long[]{ 1L, 2L, 3L })
                .putIntegerArray("int_list", new int[]{1, 2, 3})
                .putShort("short", (short) 5)
                .putString("string", "string content");

        assertEquals(compound, otherCompound);
    }

    @Test
    public void compoundsShouldNotMatch() {
        NBTCompound compound = new NBTCompound("name")
            .putInteger("test", 12)
            .putIntegerArray("list", new int[]{1, 2, 3});

        NBTCompound otherCompound = new NBTCompound("name")
                .putInteger("test", 123);

        assertNotEquals(compound, otherCompound);

    }

}
