package io.github.willqi.pizzaserver.server.level;

import io.github.willqi.pizzaserver.api.level.Level;
import io.github.willqi.pizzaserver.commons.world.Dimension;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.level.providers.BaseLevelProvider;
import io.github.willqi.pizzaserver.server.level.world.ImplWorld;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImplLevel implements Level, Closeable {

    private final ImplServer server;
    private final BaseLevelProvider provider;

    private final Map<Dimension, ImplWorld> dimensions = new HashMap<>();

    public ImplLevel(ImplServer server, BaseLevelProvider provider) {
        this.server = server;
        this.provider = provider;

        this.dimensions.put(Dimension.OVERWORLD, new ImplWorld(this, Dimension.OVERWORLD));
        this.dimensions.put(Dimension.NETHER, new ImplWorld(this, Dimension.NETHER));
        this.dimensions.put(Dimension.END, new ImplWorld(this, Dimension.END));
    }

    /**
     * Ticks all dimensions of this level
     */
    public void tick() {
        for (ImplWorld world : this.dimensions.values()) {
            world.getChunkManager().tick();
        }
    }

    @Override
    public String getName() {
        return this.getProvider().getName();
    }

    @Override
    public ImplServer getServer() {
        return this.server;
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
    }

}
