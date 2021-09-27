package io.github.willqi.pizzaserver.nbt.streams.nbt;

import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTInputStreamTest {

    @Test
    public void shouldParseTestFileCorrectly() throws IOException {
        // TODO: In the future, we should have our own test file and check for proper names and values
        //  rather than relying on the biome_definitions file
        NBTInputStream nbtInputStream = new NBTInputStream(new VarIntDataInputStream(this.getClass().getResourceAsStream("/biome_definitions.dat")));
        NBTCompound compound = nbtInputStream.readCompound();

        // Did we read all 75 tags?
        assertEquals(75, compound.keySet().size()); // all biomes were loaded

        nbtInputStream.close();
    }


}
