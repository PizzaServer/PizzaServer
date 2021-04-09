package io.github.willqi.pizzaserver;

import io.github.willqi.pizzaserver.network.ServerNetwork;
import io.github.willqi.pizzaserver.resourcepacks.ResourcePackManager;
import io.github.willqi.pizzaserver.plugin.PluginManager;
import io.github.willqi.pizzaserver.utils.Config;
import io.github.willqi.pizzaserver.utils.Logger;
import io.github.willqi.pizzaserver.utils.TimeUtils;

import java.io.*;

public class Server {

    private int targetTps;
    private int currentTps;
    private boolean running;
    private final String rootDirectory;

    private int maximumPlayersAllowed;
    private String motd;

    private String ip;
    private int port;

    private final ServerNetwork network;
    private final PluginManager pluginManager;
    private final ResourcePackManager resourcePackManager;
    private final Logger logger;

    private static Server instance;

    private Config config;

    public Server(String rootDirectory) {
        instance = this;
        this.logger = new Logger("Server");
        this.getLogger().info("Setting up PizzaServer instance.");
        this.rootDirectory = rootDirectory;

        this.network = new ServerNetwork(this);
        this.pluginManager = new PluginManager(this);
        this.resourcePackManager = new ResourcePackManager(this);

        this.setup();

        this.getLogger().info("Setup complete.");
    }

    /**
     * Start ticking the Minecraft server.
     * Does not create a new thread and will block the thread that calls this method until shutdown.
     */
    public void boot() {
        this.getLogger().info("Booting server up on " + this.getIp() + ":" + this.getPort());

        this.network.boot(this.getIp(), this.getPort());

        this.running = true;

        this.targetTps = 20;

        int currentTps = 0;
        long initNanoTime = System.nanoTime();
        long nextTpsRecording = initNanoTime + TimeUtils.secondsToNanoSeconds(1);
        long nanoSecondsPerTick = TimeUtils.secondsToNanoSeconds(1) / this.targetTps;
        long nextPredictedNanoTimeTick = initNanoTime + nanoSecondsPerTick;         // Used to determine how behind/ahead we are
        long sleepTime = TimeUtils.nanoSecondsToMilliseconds(nanoSecondsPerTick);
        while (this.running) {

            this.getNetwork().executeServerboundQueue();
            this.getNetwork().executeClientboundQueue();

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
                this.stop();
                return;
            }

            long completedNanoTime = System.nanoTime();
            if (completedNanoTime > nextTpsRecording) {
                this.currentTps = currentTps;
                currentTps = 0;
                nextTpsRecording = System.nanoTime() + TimeUtils.secondsToNanoSeconds(1);
            }
            currentTps++;

            long diff = nextPredictedNanoTimeTick - completedNanoTime;
            nanoSecondsPerTick = TimeUtils.secondsToNanoSeconds(1) / this.targetTps;
            nextPredictedNanoTimeTick = System.nanoTime() + nanoSecondsPerTick + diff;
            sleepTime = TimeUtils.nanoSecondsToMilliseconds(Math.max(nanoSecondsPerTick + diff, 0));

        }
    }

    /**
     * The server will stop after the current tick finishes.
     */
    public void stop() {
        if (this.running) {
            this.running = false;
            this.getNetwork().stop();
        } else {
            throw new RuntimeException("Server is not running");
        }
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public ServerNetwork getNetwork() {
        return this.network;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public String getMotd() {
        return this.motd;
    }

    public int getPlayerCount() {
        return 0;
    }

    public void setMaximumPlayerCount(int players) {
        this.maximumPlayersAllowed = players;
    }

    public int getMaximumPlayerCount() {
        return this.maximumPlayersAllowed;
    }

    public void setTargetTps(int newTps) {
        this.targetTps = newTps;
    }

    public int getTargetTps() {
        return this.targetTps;
    }

    public int getCurrentTps() {
        return this.currentTps;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public ResourcePackManager getResourcePackManager() {
        return this.resourcePackManager;
    }

    public String getRootDirectory() {
        return this.rootDirectory;
    }

    public Config getConfig() {
        return this.config;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public static Server getInstance() {
        return instance;
    }

    private void setup() {

        try {
            new File(this.getRootDirectory() + "/plugins").mkdirs();
            new File(this.getRootDirectory() + "/levels").mkdirs();
            new File(this.getRootDirectory() + "/players").mkdirs();
            new File(this.getRootDirectory() + "/resourcepacks").mkdirs();
        } catch (SecurityException exception) {
            throw new RuntimeException(exception);
        }

        File propertiesFile = new File(this.getRootDirectory() + "/server.yml");
        this.config = new Config();

        try {
            InputStream propertiesStream;
            if (propertiesFile.exists()) {
                propertiesStream = new FileInputStream(propertiesFile);
            } else {
                propertiesStream = this.getClass().getResourceAsStream("/server.yml");
            }
            this.config.load(propertiesStream);
            if (!propertiesFile.exists()) {
                this.config.save(new FileOutputStream(propertiesFile));
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.ip = this.config.getString("server-ip");
        this.port = this.config.getInteger("server-port");

        this.setMotd(this.config.getString("server-motd"));
        this.setMaximumPlayerCount(this.config.getInteger("player-max"));
        this.resourcePackManager.setPacksRequired(this.config.getBoolean("player-force-packs"));

    }

}
