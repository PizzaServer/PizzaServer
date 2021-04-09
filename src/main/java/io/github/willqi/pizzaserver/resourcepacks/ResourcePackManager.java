package io.github.willqi.pizzaserver.resourcepacks;

import io.github.willqi.pizzaserver.Server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ResourcePackManager {

    private final Map<UUID, ResourcePack> packs;
    private boolean required;
    private Server server;

    public ResourcePackManager(Server server) {
        this.packs = new HashMap<>();
        this.server = server;
        this.loadResourcePacks();
    }

    public boolean arePacksRequired() {
        return this.required;
    }

    public Map<UUID, ResourcePack> getPacks() {
        return Collections.unmodifiableMap(this.packs);
    }

    public void setPacksRequired(boolean required) {
        this.required = required;
    }

    private void loadResourcePacks() {
        try {
            Files.list(Paths.get(this.server.getRootDirectory() + "/resourcepacks"))
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .forEach(file -> {
                        try {
                            ResourcePack pack = new ZipResourcePack(file);
                            this.packs.put(pack.getUuid(), pack);
                            Server.getInstance().getLogger().info("Loaded resource pack: " + file.getName());
                        } catch (IOException exception) {
                            Server.getInstance().getLogger().error("Failed to load resource pack: " + file.getName());
                            Server.getInstance().getLogger().error(exception);
                        }
                    });
        } catch (IOException exception) {
            Server.getInstance().getLogger().error("Failed to read resourcepacks directory");
            Server.getInstance().getLogger().error(exception);
        }
    }

}
