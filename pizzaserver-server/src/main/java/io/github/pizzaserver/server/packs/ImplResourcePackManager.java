package io.github.pizzaserver.server.packs;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.packs.ResourcePack;
import io.github.pizzaserver.api.packs.ResourcePackManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ImplResourcePackManager implements ResourcePackManager {

    private final Map<UUID, ResourcePack> packs = new HashMap<>();
    private final Server server;
    private boolean required;

    public ImplResourcePackManager(Server server) {
        this.server = server;
    }

    @Override
    public boolean arePacksRequired() {
        return this.required;
    }

    @Override
    public Map<UUID, ResourcePack> getPacks() {
        return Collections.unmodifiableMap(this.packs);
    }

    public void setPacksRequired(boolean required) {
        this.required = required;
    }

    public void loadPacks() {
        try {
            Files.list(Paths.get(this.server.getRootDirectory() + "/resourcepacks"))
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .forEach(file -> {
                        try {
                            ResourcePack pack = new ZipResourcePack(file);
                            this.packs.put(pack.getUuid(), pack);
                            this.server.getLogger().info("Loaded resource pack: " + file.getName());
                        } catch (IOException exception) {
                            this.server.getLogger().error("Failed to load resource pack: " + file.getName(), exception);
                        }
                    });
        } catch (IOException exception) {
            this.server.getLogger().error("Failed to read resourcepacks directory", exception);
        }
    }

}
