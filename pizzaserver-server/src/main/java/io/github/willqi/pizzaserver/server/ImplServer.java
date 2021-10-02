package io.github.willqi.pizzaserver.server;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.event.EventManager;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.plugin.PluginManager;
import io.github.willqi.pizzaserver.api.scheduler.Scheduler;
import io.github.willqi.pizzaserver.api.utils.Logger;
import io.github.willqi.pizzaserver.server.level.world.blocks.VanillaBlocksLoader;
import io.github.willqi.pizzaserver.server.network.BedrockNetworkServer;
import io.github.willqi.pizzaserver.server.event.ImplEventManager;
import io.github.willqi.pizzaserver.server.network.BedrockClientSession;
import io.github.willqi.pizzaserver.server.network.handlers.LoginPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.server.packs.ImplResourcePackManager;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;
import io.github.willqi.pizzaserver.server.player.playerdata.provider.NBTPlayerDataProvider;
import io.github.willqi.pizzaserver.server.player.playerdata.provider.PlayerDataProvider;
import io.github.willqi.pizzaserver.server.plugin.ImplPluginManager;
import io.github.willqi.pizzaserver.server.utils.Config;
import io.github.willqi.pizzaserver.server.utils.ImplLogger;
import io.github.willqi.pizzaserver.server.utils.TimeUtils;
import io.github.willqi.pizzaserver.server.level.ImplLevelManager;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ImplServer implements Server {

    private static Server INSTANCE;

    private final BedrockNetworkServer network = new BedrockNetworkServer(this);
    private final Set<BedrockClientSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private final PlayerDataProvider provider = new NBTPlayerDataProvider(this);

    private final PluginManager pluginManager = new ImplPluginManager(this);
    private final ImplResourcePackManager dataPackManager = new ImplResourcePackManager(this);
    private final ImplLevelManager levelManager = new ImplLevelManager(this);
    private final EventManager eventManager = new ImplEventManager(this);

    private final Set<Scheduler> syncedSchedulers = Collections.synchronizedSet(new HashSet<>());
    private final Scheduler scheduler = new Scheduler(this, 1);

    private final Logger logger;

    private final CountDownLatch shutdownLatch = new CountDownLatch(1);

    private int targetTps;
    private int currentTps;
    private volatile boolean running;
    private final String rootDirectory;

    private int maximumPlayersAllowed;
    private String motd;

    private String ip;
    private int port;

    private ServerConfig config;


    public ImplServer(String rootDirectory) {
        INSTANCE = this;
        this.rootDirectory = rootDirectory;
        this.setupFiles();

        this.logger = new ImplLogger("Server");

        Runtime.getRuntime().addShutdownHook(new ServerExitListener());

        // TODO: load plugins
    }

    /**
     * Start ticking the Minecraft server.
     * Does not create a new thread and will block the thread that calls this method until shutdown.
     */
    public void boot() {
        ServerProtocol.loadVersions();
        VanillaBlocksLoader.load();

        this.getResourcePackManager().loadPacks();
        this.setTargetTps(20);

        try {
            this.getNetwork().boot(this.getIp(), this.getPort());
        } catch (InterruptedException | ExecutionException exception) {
            throw new RuntimeException(exception);
        }
        this.running = true;
        this.scheduler.startScheduler();

        int currentTps = 0;
        long nextTpsRecording = 0;

        // The amount of nanoseconds to sleep for
        // This fluctuates depending on if we were at a slower/faster tps before
        long sleepTime = 0;

        while (this.running) {
            long idealNanoSleepPerTick = TimeUtils.secondsToNanoSeconds(1) / this.targetTps;

            // Figure out how long it took to tick
            long startTickTime = System.nanoTime();
            this.tick();
            currentTps++;
            long timeTakenToTick = System.nanoTime() - startTickTime;

            // Sleep for the ideal time but take into account the time spent running the tick
            sleepTime += idealNanoSleepPerTick - timeTakenToTick;
            long sleepStart = System.nanoTime();
            try {
                Thread.sleep(Math.max(TimeUtils.nanoSecondsToMilliseconds(sleepTime), 0));
            } catch (InterruptedException exception) {
                exception.printStackTrace();
                this.stop();
                return;
            }

            // How long did it actually take to sleep?
            // If we didn't sleep for the correct amount,
            // take that into account for the next sleep by
            // leaving extra/less for the next sleep.
            sleepTime -= System.nanoTime() - sleepStart;

            // Record TPS every second
            if (System.nanoTime() > nextTpsRecording) {
                this.currentTps = currentTps;
                currentTps = 0;
                nextTpsRecording = System.nanoTime() + TimeUtils.secondsToNanoSeconds(1);
            }
        }
        this.stop();
    }

    private void tick() {
        this.processPackets();

        try {
            this.getLevelManager().tick();
        } catch (Exception exception) {
            this.getLogger().error("Error occurred while ticking", exception);
            this.stop();
            return;
        }

        for (Scheduler scheduler : this.syncedSchedulers) {
            try {
                scheduler.serverTick();
            } catch (Exception exception) {
                this.getLogger().error("Failed to tick scheduler", exception);
            }
        }
    }

    /**
     * Processes incoming and outgoing packets.
     */
    private void processPackets() {
        synchronized (this.sessions) {
            // Process all packets that are outgoing and incoming
            Iterator<BedrockClientSession> sessions = this.sessions.iterator();
            while (sessions.hasNext()) {
                BedrockClientSession session = sessions.next();
                try {
                    session.processIncomingPackets();
                } catch (Exception exception) {
                    session.disconnect();
                    this.getLogger().error("Disconnecting session due to failure in processing packets", exception);
                }

                // check if the client disconnected
                if (session.isDisconnected()) {
                    sessions.remove();
                    ImplPlayer player = session.getPlayer();
                    if (player != null) {
                        player.onDisconnect();
                        this.getNetwork().updatePong();
                    }
                }
            }
        }
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
        try {
            this.getLevelManager().close();
        } catch (IOException exception) {
            this.getLogger().error("Failed to close LevelManager", exception);
        }

        // We're done stop operations. Exit program.
        this.shutdownLatch.countDown();
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public BedrockNetworkServer getNetwork() {
        return this.network;
    }

    public void registerSession(BedrockClientSession session) {
        session.addPacketHandler(new LoginPacketHandler(this, session));
        this.sessions.add(session);
    }

    @Override
    public String getMotd() {
        return this.motd;
    }

    @Override
    public void setMotd(String motd) {
        this.motd = motd;
    }

    @Override
    public Set<Player> getPlayers() {
        return this.sessions.stream()
                .map(BedrockClientSession::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public int getPlayerCount() {
        return this.getPlayers().size();
    }

    @Override
    public void setMaximumPlayerCount(int players) {
        this.maximumPlayersAllowed = players;
    }

    @Override
    public int getMaximumPlayerCount() {
        return this.maximumPlayersAllowed;
    }

    @Override
    public int getTargetTps() {
        return this.targetTps;
    }

    @Override
    public void setTargetTps(int newTps) {
        this.targetTps = newTps;
    }

    @Override
    public int getCurrentTps() {
        return this.currentTps;
    }

    @Override
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    @Override
    public ImplResourcePackManager getResourcePackManager() {
        return this.dataPackManager;
    }

    @Override
    public ImplLevelManager getLevelManager() {
        return this.levelManager;
    }

    @Override
    public EventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public Set<Scheduler> getSyncedSchedulers() {
        return Collections.unmodifiableSet(this.syncedSchedulers);
    }

    @Override
    public void syncScheduler(Scheduler scheduler) {
        if (scheduler.isRunning()) {
            this.syncedSchedulers.add(scheduler);
        }
    }

    @Override
    public boolean desyncScheduler(Scheduler scheduler) {
        return this.syncedSchedulers.remove(scheduler);
    }

    /**
     * Retrieve the {@link PlayerDataProvider} used to save and store player data.
     * @return {@link PlayerDataProvider}
     */
    public PlayerDataProvider getPlayerProvider() {
        return this.provider;
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public String getRootDirectory() {
        return this.rootDirectory;
    }

    public ServerConfig getConfig() {
        return this.config;
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
            new File(this.getRootDirectory() + "/levels").mkdirs();
            new File(this.getRootDirectory() + "/players").mkdirs();
            new File(this.getRootDirectory() + "/resourcepacks").mkdirs();
        } catch (SecurityException exception) {
            throw new RuntimeException(exception);
        }

        File configFile = new File(this.getRootDirectory() + "/server.yml");
        Config config = new Config();

        try {
            if (!configFile.exists()) {
                Files.copy(this.getClass().getResourceAsStream("/server.yml"), configFile.toPath());
            }

            try (FileInputStream inputStream = new FileInputStream(configFile)) {
                config.load(inputStream);
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
            if (ImplServer.this.running) {
                ImplServer.this.running = false;
                try {
                    ImplServer.this.shutdownLatch.await();
                } catch (InterruptedException exception) {
                    ImplServer.getInstance().getLogger().error("Exit listener exception", exception);
                }
            }
        }

    }

}
