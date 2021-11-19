package io.github.pizzaserver.server.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigTests {

    @Test
    public void shouldParseExampleFileWithoutErrors() {
        Assertions.assertDoesNotThrow(ConfigTests::getConfig);
    }

    @Test
    public void shouldCorrectlySaveFile() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Config currentConfig = getConfig();
        currentConfig.save(stream);

        InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        Config writtenConfig = new Config();
        writtenConfig.load(inputStream);

        assertEquals(currentConfig.getProperties(), writtenConfig.getProperties());
    }

    @Test
    public void shouldRetrieveGroupProperties() {
        Config config = getConfig();
        assertEquals(config.getGroup("group").getString("inner-property"), "This is a value");
        assertEquals(config.getGroup("group").getGroup("inner-group").getString("nested-child"), "nested child");
    }

    @Test
    public void shouldRetrieveStringProperty() {
        Config config = getConfig();
        assertEquals(config.getString("property"), "value");
    }

    @Test
    public void shouldRetrieveStringList() {
        Config config = getConfig();
        assertEquals(config.getStringList("string-list"), Arrays.asList("a", "b", "c"));
    }

    @Test
    public void shouldRetrieveIntProperty() {
        Config config = getConfig();
        assertEquals(config.getInteger("int-property"), 42);
    }

    @Test
    public void shouldRetrieveIntList() {
        Config config = getConfig();
        assertEquals(config.getIntegerList("int-list"), Arrays.asList(1, 2, 3));
    }


    @Test
    public void shouldRetrieveBooleanProperty() {
        Config config = getConfig();
        assertEquals(config.getBoolean("boolean-property"), true);
    }

    @Test
    public void shouldRetrieveBooleanList() {
        Config config = getConfig();
        assertEquals(config.getBooleanList("boolean-list"), Arrays.asList(true, true, false, true));
    }

    @Test
    public void shouldRetrieveDoubleValue() {
        Config config = getConfig();
        assertEquals(config.getDouble("decimal-value"), 1.2345678901234567D);
    }

    @Test
    public void shouldRetrieveDoubleList() {
        Config config = getConfig();
        assertEquals(config.getDoubleList("decimal-list"), Arrays.asList(1.23D, 1.2345678901234567D));
    }


    @Test
    public void shouldRetrieveLongValue() {
        Config config = getConfig();
        assertEquals(config.getLong("long-property"), 21474836471337L);
    }

    @Test
    public void shouldRetrieveLongList() {
        Config config = getConfig();
        assertEquals(config.getLongList("long-list"), Arrays.asList(21474836471337L, -21474836471337L));
    }


    private static Config getConfig() throws RuntimeException {
        Config config = new Config();
        config.load(ConfigTests.class.getResourceAsStream("/config.yml"));
        return config;
    }

}
