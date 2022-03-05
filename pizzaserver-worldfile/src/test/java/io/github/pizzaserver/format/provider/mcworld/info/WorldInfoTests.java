package io.github.pizzaserver.format.provider.mcworld.info;

import io.github.pizzaserver.format.LevelData;
import io.github.pizzaserver.format.provider.ResourceUtils;
import io.github.pizzaserver.format.provider.mcworld.MCWorldLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorldInfoTests {

    @Test
    public void shouldParseTestWorldInfo(@TempDir Path temporaryDir) throws IOException {
        ResourceUtils.extractZipContents("testworld", temporaryDir);
        MCWorldLevel world = new MCWorldLevel(temporaryDir.toFile());
        try {
            LevelData info = world.getLevelData();

            assertEquals("1.16.100 World Test", info.getName());
            assertTrue(info.getPlayerAbilities().canAttackPlayers());
        } finally {
            world.close();
            ResourceUtils.deleteDirectoryContents(temporaryDir);
        }
    }

}
