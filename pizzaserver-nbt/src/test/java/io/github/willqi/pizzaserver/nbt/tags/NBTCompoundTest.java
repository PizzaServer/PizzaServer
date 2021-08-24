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
            compound.putCompound("a", innerCompound);
            compound = innerCompound;
        }

        NBTCompound finalCompound = compound;
        assertThrows(NBTLimitException.class, () -> finalCompound.putCompound("a", new NBTCompound()));
    }

    @Test
    public void shouldParseCorrectly() throws IOException {

        // Create test compound
        NBTCompound testCompound = new NBTCompound()
                .putDouble("double", 12d)
                .putInteger("int", 2);

        NBTCompound innerCompound = new NBTCompound()
                .putFloat("float", 12f);
        testCompound.putCompound("innerCompound", innerCompound);

        // Write and read the result
        ByteArrayOutputStream resultingByteStream = new ByteArrayOutputStream();
        NBTOutputStream outputStream = new NBTOutputStream(resultingByteStream);
        outputStream.writeCompound(testCompound);
        NBTInputStream inputStream = new NBTInputStream(new ByteArrayInputStream(resultingByteStream.toByteArray()));

        // Confirm results are correct
        NBTCompound rebuiltCompound = inputStream.readCompound();
        double nbtDouble = rebuiltCompound.getDouble("double");
        int nbtInteger = rebuiltCompound.getInteger("int");
        NBTCompound rebuildInnerCompound = rebuiltCompound.getCompound("innerCompound");

        assertEquals(12d, nbtDouble);
        assertEquals(2, nbtInteger);
        assertEquals(12f, rebuildInnerCompound.getFloat("float"));

        outputStream.close();
        inputStream.close();
    }

}
