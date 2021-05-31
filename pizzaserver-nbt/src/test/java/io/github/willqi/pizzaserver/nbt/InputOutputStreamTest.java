package io.github.willqi.pizzaserver.nbt;

import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.nbt.tags.NBTDouble;
import io.github.willqi.pizzaserver.nbt.tags.NBTInteger;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class InputOutputStreamTest {

    @Test
    public void shouldBeAbleToParseOutput() throws IOException {

        NBTCompound testCompound = new NBTCompound();
        testCompound.put("double", new NBTDouble(12d));
        testCompound.put("int", new NBTInteger(2));

        NBTOutputStream outputStream = new NBTOutputStream();
        outputStream.writeCompound(testCompound);
        NBTInputStream inputStream = new NBTInputStream(new ByteArrayInputStream(outputStream.getBytes()));

        NBTCompound rebuiltCompound = inputStream.readCompound();
        NBTDouble nbtDouble = rebuiltCompound.getDouble("double");
        NBTInteger nbtInteger = rebuiltCompound.getInteger("int");

        assertEquals(12d, nbtDouble.getValue());
        assertEquals(2, nbtInteger.getValue());

        outputStream.close();
        inputStream.close();

    }

}
