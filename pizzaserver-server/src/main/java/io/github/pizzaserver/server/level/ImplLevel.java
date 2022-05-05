package io.github.pizzaserver.server.level;

import com.nukkitx.protocol.bedrock.packet.SetTimePacket;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.level.Level;
import io.github.pizzaserver.api.level.LevelGameRules;
import io.github.pizzaserver.api.level.data.Difficulty;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.format.BedrockLevel;
import io.github.pizzaserver.format.data.LevelData;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.level.world.ImplWorld;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ImplLevel implements Level, Closeable {

    private final ImplLevelManager levelManager;
    private final LevelData levelData;
    private final BedrockLevel provider;
    private final LevelGameRules gameRules = new ImplLevelGameRules(this);

    private final Map<Dimension, ImplWorld> dimensions = new HashMap<>();

    private Difficulty difficulty;
    private int time;

    public ImplLevel(ImplLevelManager levelManager, BedrockLevel provider) {
        this.levelManager = levelManager;
        this.provider = provider;

        this.levelData = provider.getLevelData();

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

        if (this.getGameRules().isDaylightCycleEnabled()) {
            this.time++;
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

    @Override
    public Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();
        for (World world : this.dimensions.values()) {
            players.addAll(world.getPlayers());
        }

        return players;
    }

    @Override
    public boolean isDay() {
        int time = this.getTime() % 24000;
        return time < 12000;
    }

    @Override
    public int getTime() {
        return this.time;
    }

    @Override
    public void setTime(int time) {
        this.time = time >= 0 ? time : 24000 + time;

        SetTimePacket setTimePacket = new SetTimePacket();
        setTimePacket.setTime(time);
        for (Player player : this.getPlayers()) {
            player.sendPacket(setTimePacket);
        }
    }

    public BedrockLevel getProvider() {
        return this.provider;
    }

    public LevelData getLevelData() {
        return this.levelData;
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
    public LevelGameRules getGameRules() {
        return this.gameRules;
    }

    @Override
    public void save() throws IOException {
        for (ImplWorld world : this.dimensions.values()) {
            world.save();
        }

        this.getProvider().setLevelData(this.getLevelData());
    }

    @Override
    public void close() throws IOException {
        if (Server.getInstance().getConfig().isSavingEnabled()) {
            this.save();
        }

        for (ImplWorld world : this.dimensions.values()) {
            world.close();
        }
        this.getProvider().close();
    }

}
