package io.github.willqi.pizzaserver.server;

import io.github.willqi.pizzaserver.server.event.EventManager;
import io.github.willqi.pizzaserver.server.network.BedrockClientSession;
import io.github.willqi.pizzaserver.server.network.BedrockServer;
import io.github.willqi.pizzaserver.server.network.handlers.LoginPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.server.packs.DataPackManager;
import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.plugin.PluginManager;
import io.github.willqi.pizzaserver.server.utils.Config;
import io.github.willqi.pizzaserver.server.utils.Logger;
import io.github.willqi.pizzaserver.server.utils.TimeUtils;
import io.github.willqi.pizzaserver.server.world.WorldManager;
import io.github.willqi.pizzaserver.server.world.blocks.BlockRegistry;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Server {

    private static Server INSTANCE;

    private final BedrockServer network = new BedrockServer(this);
    private final PluginManager pluginManager = new PluginManager(this);
    private final DataPackManager dataPackManager = new DataPackManager(this);
    private final WorldManager worldManager = new WorldManager(this);
    private final EventManager eventManager = new EventManager(this);

    private final BlockRegistry blockRegistry = new BlockRegistry();

    private final Logger logger = new Logger("Server");

    private final Set<BedrockClientSession> sessions = Collections.synchronizedSet(new HashSet<>());

    private final Thread serverExitListener = new ServerExitListener();
    private volatile boolean stopByConsoleExit;

    private int targetTps;
    private int currentTps;
    private volatile boolean running;
    private final String rootDirectory;

    private int maximumPlayersAllowed;
    private String motd;

    private String ip;
    private int port;

    private ServerConfig config;


    public Server(String rootDirectory) {
        INSTANCE = this;
        this.getLogger().info("Setting up PizzaServer instance.");
        Runtime.getRuntime().addShutdownHook(serverExitListener);

        this.rootDirectory = rootDirectory;

        // Load required data/files
        this.setupFiles();

        this.getLogger().info("Internal setup complete.");

        // TODO: load plugins
    }

    /**
     * Start ticking the Minecraft server.
     * Does not create a new thread and will block the thread that calls this method until shutdown.
     */
    public void boot() {
        this.stopByConsoleExit = false;
        ServerProtocol.loadVersions();
        this.getResourcePackManager().loadResourcePacks();
        this.getResourcePackManager().loadBehaviorPacks();
        this.setTargetTps(20);

        this.getWorldManager().loadWorlds();

        try {
            this.getNetwork().boot(this.getIp(), this.getPort());
        } catch (InterruptedException | ExecutionException exception) {
            throw new RuntimeException(exception);
        }
        this.running = true;

        int currentTps = 0;
        long initNanoTime = System.nanoTime();
        long nextTpsRecording = initNanoTime + TimeUtils.secondsToNanoSeconds(1);
        long nanoSecondsPerTick = TimeUtils.secondsToNanoSeconds(1) / this.targetTps;
        long nextPredictedNanoTimeTick = initNanoTime + nanoSecondsPerTick;         // Used to determine how behind/ahead we are
        long sleepTime = TimeUtils.nanoSecondsToMilliseconds(nanoSecondsPerTick);
        while (this.running) {
            synchronized (this.sessions) {
                Iterator<BedrockClientSession> sessions = this.sessions.iterator();
                while (sessions.hasNext()) {
                    BedrockClientSession session = sessions.next();
                    session.processPackets();

                    if (session.isDisconnected()) {
                        Player player = session.getPlayer();
                        if (player != null) {
                            player.onDisconnect();
                        }
                        sessions.remove();
                    }
                }
            }

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
        this.stop();
    }

    /**
     * The server will stop after the current tick finishes.
     */
    private void stop() {
        this.getLogger().info("Stopping server...");

        for (BedrockClientSession session : this.sessions) {
            if (session.getPlayer() != null) {
                session.getPlayer().disconnect("Server Stopped");
            } else {
                session.disconnect();
            }
        }

        this.getNetwork().stop();
        this.getWorldManager().unloadWorlds();

        // We're done stop operations. Exit program.
        if (this.stopByConsoleExit) {   // Ensure that the notify is called AFTER the thread is in the waiting state.
            while (this.serverExitListener.getState() != Thread.State.WAITING) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }
        }

        synchronized (this.serverExitListener) {
            this.serverExitListener.notify();
        }
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public BedrockServer getNetwork() {
        return this.network;
    }

    public void registerSession(BedrockClientSession session) {
        session.setPacketHandler(new LoginPacketHandler(this, session));
        this.sessions.add(session);
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public String getMotd() {
        return this.motd;
    }

    /**
     * Return all players who been spawned into the world
     * @return set of players
     */
    public Set<Player> getPlayers() {
        return this.sessions.stream()
                .map(BedrockClientSession::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public int getPlayerCount() {
        return this.getPlayers().size();
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

    public DataPackManager getResourcePackManager() {
        return this.dataPackManager;
    }

    public WorldManager getWorldManager() {
        return this.worldManager;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }

    public BlockRegistry getBlockRegistry() {
        return this.blockRegistry;
    }

    public String getRootDirectory() {
        return this.rootDirectory;
    }

    public ServerConfig getConfig() {
        return this.config;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public static Server getInstance() {
        return INSTANCE;
    }

    /**
     * Called to load and setup required files/classes.
     */
    private void setupFiles() {

        try {
            new File(this.getRootDirectory() + "/plugins").mkdirs();
            new File(this.getRootDirectory() + "/worlds").mkdirs();
            new File(this.getRootDirectory() + "/players").mkdirs();
            new File(this.getRootDirectory() + "/resourcepacks").mkdirs();
            new File(this.getRootDirectory() + "/behaviorpacks").mkdirs();
        } catch (SecurityException exception) {
            throw new RuntimeException(exception);
        }

        File propertiesFile = new File(this.getRootDirectory() + "/server.yml");
        Config config = new Config();

        try {
            InputStream propertiesStream;
            if (propertiesFile.exists()) {
                propertiesStream = new FileInputStream(propertiesFile);
            } else {
                propertiesStream = this.getClass().getResourceAsStream("/server.yml");
            }
            config.load(propertiesStream);
            if (!propertiesFile.exists()) {
                config.save(new FileOutputStream(propertiesFile));
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.config = new ServerConfig(config);

        this.ip = this.config.getIp();
        this.port = this.config.getPort();

        this.setMotd(this.config.getMotd());
        this.setMaximumPlayerCount(this.config.getMaximumPlayers());
        this.dataPackManager.setPacksRequired(this.config.arePacksForced());

    }


    private class ServerExitListener extends Thread {

        @Override
        public void run() {
            if (Server.this.running) {
                Server.this.stopByConsoleExit = true;
                Server.this.running = false;
                try {
                    synchronized (this) {
                        Thread.currentThread().wait();
                    }
                } catch (InterruptedException exception) {
                    Server.getInstance().getLogger().error("Exit listener exception", exception);
                }
            }
        }
    }

}
