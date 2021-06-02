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

public class NBTCompoundTest {

    @Test
    public void cannotGoDeeperThan512() {
        NBTCompound compound = new NBTCompound();
        for (int i = 0; i < 512; i++) {
            NBTCompound innerCompound = new NBTCompound();
            compound.put("a", innerCompound);
            compound = innerCompound;
        }

        NBTCompound finalCompound = compound;
        assertThrows(NBTLimitException.class, () -> finalCompound.put("a", new NBTCompound()));
    }

    @Test
    public void shouldParseCorrectly() throws IOException {
        NBTCompound testCompound = new NBTCompound();
        testCompound.put("double", new NBTDouble(12d));
        testCompound.put("int", new NBTInteger(2));

        ByteArrayOutputStream resultingByteStream = new ByteArrayOutputStream();
        NBTOutputStream outputStream = new NBTOutputStream(resultingByteStream);
        outputStream.writeCompound(testCompound);
        NBTInputStream inputStream = new NBTInputStream(new ByteArrayInputStream(resultingByteStream.toByteArray()));

        NBTCompound rebuiltCompound = inputStream.readCompound();
        NBTDouble nbtDouble = rebuiltCompound.getDouble("double");
        NBTInteger nbtInteger = rebuiltCompound.getInteger("int");

        assertEquals(12d, nbtDouble.getValue());
        assertEquals(2, nbtInteger.getValue());

        outputStream.close();
        inputStream.close();
    }

}
