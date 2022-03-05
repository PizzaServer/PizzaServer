package io.github.pizzaserver.format;

import io.github.pizzaserver.format.data.LevelData;
import io.github.pizzaserver.format.provider.ResourceUtils;
import io.github.pizzaserver.format.provider.mcworld.MCWorldLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BedrockLevelTests {

    @Test
    public void shouldParseTestWorldInfo(@TempDir Path temporaryDir) throws IOException {
        ResourceUtils.extractZipContents("testworld", temporaryDir);
        try (MCWorldLevel world = new MCWorldLevel(temporaryDir.toFile())) {
            LevelData info = world.getLevelData();

            assertEquals("1.16.100 World Test", info.getName());
            assertTrue(info.getPlayerAbilities().canAttackPlayers());
        } finally {
            ResourceUtils.deleteDirectoryContents(temporaryDir);
        }
    }

    @Test
    public void showSaveWorldInfo(@TempDir Path temporaryDir) throws IOException {
        ResourceUtils.extractZipContents("testworld", temporaryDir);
        try (MCWorldLevel world = new MCWorldLevel(temporaryDir.toFile())) {
            LevelData data = world.getLevelData();

            data.setCurrentTick(1000);
            data.setName("New Name!");

            world.setLevelData(data);
        }

        try (MCWorldLevel world = new MCWorldLevel(temporaryDir.toFile())) {
            LevelData data = world.getLevelData();

            assertEquals(1000, data.getCurrentTick());
            assertEquals("New Name!", data.getName());
        }
    }

}
