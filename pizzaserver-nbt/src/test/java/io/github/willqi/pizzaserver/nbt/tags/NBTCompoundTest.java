package io.github.willqi.pizzaserver.nbt.tags;

import io.github.willqi.pizzaserver.nbt.exceptions.NBTLimitException;
import org.junit.jupiter.api.Test;

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
        assertThrows(NBTLimitException.class, () -> {
            finalCompound.put("a", new NBTCompound());
        });
    }

}
