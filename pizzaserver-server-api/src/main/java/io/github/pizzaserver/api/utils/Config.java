package io.github.pizzaserver.api.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Config extends ConfigGroup {

    public void load(InputStream stream) {
        this.properties = new Yaml().load(stream);
    }

    public void save(String saveLocation) {
        try (OutputStream stream = new FileOutputStream(saveLocation)) {
            this.save(stream);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    public void save(OutputStream stream) {
        try {
            stream.write(new Yaml().dump(this.properties).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
