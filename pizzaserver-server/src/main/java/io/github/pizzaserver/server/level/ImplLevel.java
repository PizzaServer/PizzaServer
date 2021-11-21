package io.github.pizzaserver.server.level;

import io.github.pizzaserver.api.level.Level;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.level.providers.BaseLevelProvider;
import io.github.pizzaserver.server.level.world.ImplWorld;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImplLevel implements Level, Closeable {

    private final ImplLevelManager levelManager;
    private final BaseLevelProvider provider;

    private final Map<Dimension, ImplWorld> dimensions = new HashMap<>();

    public ImplLevel(ImplLevelManager levelManager, BaseLevelProvider provider) {
        this.levelManager = levelManager;
        this.provider = provider;

        this.dimensions.put(Dimension.OVERWORLD, new ImplWorld(this, Dimension.OVERWORLD));
        this.dimensions.put(Dimension.NETHER, new ImplWorld(this, Dimension.NETHER));
        this.dimensions.put(Dimension.END, new ImplWorld(this, Dimension.END));
    }

    /**
     * Ticks all dimensions of this level.
     */
    public void tick() {
        for (ImplWorld world : this.dimensions.values()) {
            world.tick();
        }
    }

    @Override
    public ImplLevelManager getLevelManager() {
        return this.levelManager;
    }

    @Override
    public String getName() {
        return this.getProvider().getLevelData().getName();
    }

    @Override
    public ImplServer getServer() {
        return this.levelManager.getServer();
    }

    public BaseLevelProvider getProvider() {
        return this.provider;
    }

    @Override
    public ImplWorld getDimension(Dimension dimension) {
        return this.dimensions.getOrDefault(dimension, null);
    }

    @Override
    public void save() throws IOException {

    }

    @Override
    public void close() throws IOException {
        for (ImplWorld world : this.dimensions.values()) {
            world.close();
        }
        this.getProvider().close();
    }

}
