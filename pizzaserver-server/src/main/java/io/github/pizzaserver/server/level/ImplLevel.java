package io.github.pizzaserver.server.level;

import io.github.pizzaserver.api.level.Level;
import io.github.pizzaserver.api.level.data.Difficulty;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.format.BedrockLevel;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.level.world.ImplWorld;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImplLevel implements Level, Closeable {

    private final ImplLevelManager levelManager;
    private final BedrockLevel provider;

    private final Map<Dimension, ImplWorld> dimensions = new HashMap<>();

    private Difficulty difficulty;

    public ImplLevel(ImplLevelManager levelManager, BedrockLevel provider) {
        this.levelManager = levelManager;
        this.provider = provider;

        this.dimensions.put(Dimension.OVERWORLD, new ImplWorld(this, Dimension.OVERWORLD));
        this.dimensions.put(Dimension.NETHER, new ImplWorld(this, Dimension.NETHER));
        this.dimensions.put(Dimension.END, new ImplWorld(this, Dimension.END));

        this.difficulty = Difficulty.values()[this.getProvider().getLevelData().getDifficulty()];
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

    public BedrockLevel getProvider() {
        return this.provider;
    }

    @Override
    public ImplWorld getDimension(Dimension dimension) {
        return this.dimensions.getOrDefault(dimension, null);
    }

    @Override
    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public void save() throws IOException {
        // TODO: save chunks
        // TODO: save level data
    }

    @Override
    public void close() throws IOException {
        for (ImplWorld world : this.dimensions.values()) {
            world.close();
        }
        this.getProvider().close();
    }

}
