package io.github.willqi.pizzaserver.server.packs;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.packs.ResourcePackManager;
import io.github.willqi.pizzaserver.api.packs.ResourcePack;
import io.github.willqi.pizzaserver.server.ImplServer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
                            ImplServer.getInstance().getLogger().info("Loaded resource pack: " + file.getName());
                        } catch (IOException exception) {
                            ImplServer.getInstance().getLogger().error("Failed to load resource pack: " + file.getName(), exception);
                        }
                    });
        } catch (IOException exception) {
            ImplServer.getInstance().getLogger().error("Failed to read resourcepacks directory", exception);
        }
    }

}
