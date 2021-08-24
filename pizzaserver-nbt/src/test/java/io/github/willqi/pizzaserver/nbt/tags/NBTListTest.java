package io.github.willqi.pizzaserver.nbt.tags;

import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.exceptions.NBTLimitException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NBTListTest {

    @Test
    public void cannotGoDeeperThan512() {
        NBTList<NBTList<?>> list = new NBTList<>(NBTTag.LIST_TAG_ID);
        for (int i = 0; i < 512; i++) {
            NBTList<NBTList<?>> innerList = new NBTList<>(NBTTag.LIST_TAG_ID);
            list.setContents(new NBTList[]{ innerList });
            list = innerList;
        }

        NBTList<NBTList<?>> finalList = list;
        assertThrows(NBTLimitException.class, () -> finalList.setContents(new NBTList[]{ new NBTList<>(NBTTag.LIST_TAG_ID) }));
    }

    @Test
    public void shouldParseCorrectly() throws IOException {

        // Create test input
        NBTList<Integer> list = new NBTList<>(NBTTag.INT_TAG_ID);
        list.setContents(new Integer[]{ 1, 2, 3 });

        // Write and read it
        ByteArrayOutputStream resultingByteStream = new ByteArrayOutputStream();
        NBTOutputStream outputStream = new NBTOutputStream(resultingByteStream);
        outputStream.writeList(list);

        NBTInputStream inputStream = new NBTInputStream(new ByteArrayInputStream(resultingByteStream.toByteArray()));
        NBTList<Integer> rebuiltList =  inputStream.readList();

        // Confirm results
        assertEquals(1, rebuiltList.getContents()[0]);
        assertEquals(2, rebuiltList.getContents()[1]);
        assertEquals(3, rebuiltList.getContents()[2]);

    }


}
