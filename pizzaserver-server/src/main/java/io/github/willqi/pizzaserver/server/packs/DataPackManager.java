package io.github.willqi.pizzaserver.server.packs;

import io.github.willqi.pizzaserver.api.APIServer;
import io.github.willqi.pizzaserver.api.packs.APIDataPackManager;
import io.github.willqi.pizzaserver.api.packs.APIDataPack;
import io.github.willqi.pizzaserver.server.Server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DataPackManager implements APIDataPackManager {

    private final Map<UUID, APIDataPack> resourcePacks = new HashMap<>();
    private final Map<UUID, APIDataPack> behaviorPacks = new HashMap<>();
    private final APIServer server;
    private boolean required;

    public DataPackManager(APIServer server) {
        this.server = server;
    }

    @Override
    public boolean arePacksRequired() {
        return this.required;
    }

    @Override
    public Map<UUID, APIDataPack> getResourcePacks() {
        return Collections.unmodifiableMap(this.resourcePacks);
    }

    @Override
    public Map<UUID, APIDataPack> getBehaviourPacks() {
        return Collections.unmodifiableMap(this.behaviorPacks);
    }

    public void setPacksRequired(boolean required) {
        this.required = required;
    }

    public void loadResourcePacks() {
        try {
            Files.list(Paths.get(this.server.getRootDirectory() + "/resourcepacks"))
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .forEach(file -> {
                        try {
                            APIDataPack pack = new ZipDataPack(file);
                            this.resourcePacks.put(pack.getUuid(), pack);
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

    public void loadBehaviorPacks() {
        try {
            Files.list(Paths.get(this.server.getRootDirectory() + "/behaviorpacks"))
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .forEach(file -> {
                        try {
                            APIDataPack pack = new ZipDataPack(file);
                            this.behaviorPacks.put(pack.getUuid(), pack);
                            Server.getInstance().getLogger().info("Loaded behavior pack: " + file.getName());
                        } catch (IOException exception) {
                            Server.getInstance().getLogger().error("Failed to load behavior pack: " + file.getName());
                            Server.getInstance().getLogger().error(exception);
                        }
                    });
        } catch (IOException exception) {
            Server.getInstance().getLogger().error("Failed to read behaviorpacks directory");
            Server.getInstance().getLogger().error(exception);
        }
    }

}
