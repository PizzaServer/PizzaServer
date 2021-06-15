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
        NBTList<NBTList<? extends NBTTag>> list = new NBTList<>(NBTList.ID);
        for (int i = 0; i < 512; i++) {
            NBTList<NBTList<? extends NBTTag>> innerList = new NBTList<>(NBTList.ID);
            list.setContents(new NBTList[]{ innerList });
            list = innerList;
        }

        NBTList<NBTList<? extends NBTTag>> finalList = list;
        assertThrows(NBTLimitException.class, () -> finalList.setContents(new NBTList[]{ new NBTList<>(NBTList.ID) }));
    }

    @Test
    public void shouldParseCorrectly() throws IOException {

        // Create test input
        NBTList<NBTInteger> list = new NBTList<>(NBTInteger.ID);
        list.setContents(new NBTInteger[]{ new NBTInteger(1), new NBTInteger(2), new NBTInteger(3) });

        // Write and read it
        ByteArrayOutputStream resultingByteStream = new ByteArrayOutputStream();
        NBTOutputStream outputStream = new NBTOutputStream(resultingByteStream);
        outputStream.writeList(list);

        NBTInputStream inputStream = new NBTInputStream(new ByteArrayInputStream(resultingByteStream.toByteArray()));
        NBTList<NBTInteger> rebuiltList =  (NBTList<NBTInteger>)inputStream.readList();

        // Confirm results
        assertEquals(1, rebuiltList.getContents()[0].getValue());
        assertEquals(2, rebuiltList.getContents()[1].getValue());
        assertEquals(3, rebuiltList.getContents()[2].getValue());

    }


}
