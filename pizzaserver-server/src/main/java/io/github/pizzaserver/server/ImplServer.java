package io.github.pizzaserver.server;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.ServerConfig;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.boss.BossBar;
import io.github.pizzaserver.api.entity.inventory.BlockEntityInventory;
import io.github.pizzaserver.api.entity.inventory.EntityInventory;
import io.github.pizzaserver.api.event.EventManager;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.plugin.PluginManager;
import io.github.pizzaserver.api.scheduler.Scheduler;
import io.github.pizzaserver.api.scoreboard.Scoreboard;
import io.github.pizzaserver.api.utils.Config;
import io.github.pizzaserver.api.utils.Logger;
import io.github.pizzaserver.server.block.ImplBlockRegistry;
import io.github.pizzaserver.server.blockentity.ImplBlockEntityRegistry;
import io.github.pizzaserver.server.entity.ImplEntityRegistry;
import io.github.pizzaserver.server.entity.boss.ImplBossBar;
import io.github.pizzaserver.server.entity.inventory.ImplBlockEntityInventory;
import io.github.pizzaserver.server.entity.inventory.ImplEntityInventory;
import io.github.pizzaserver.server.entity.inventory.InventoryUtils;
import io.github.pizzaserver.server.event.ImplEventManager;
import io.github.pizzaserver.server.item.ImplItemRegistry;
import io.github.pizzaserver.server.level.ImplLevelManager;
import io.github.pizzaserver.server.network.BedrockNetworkServer;
import io.github.pizzaserver.server.network.protocol.PlayerSession;
import io.github.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.pizzaserver.server.packs.ImplResourcePackManager;
import io.github.pizzaserver.server.player.handlers.LoginHandshakePacketHandler;
import io.github.pizzaserver.server.player.playerdata.provider.NBTPlayerDataProvider;
import io.github.pizzaserver.server.player.playerdata.provider.PlayerDataProvider;
import io.github.pizzaserver.server.plugin.ImplPluginManager;
import io.github.pizzaserver.server.scoreboard.ImplScoreboard;
import io.github.pizzaserver.server.utils.ImplLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ImplServer extends Server {

    protected BlockRegistry blockRegistry = new ImplBlockRegistry();
    protected ItemRegistry itemRegistry = new ImplItemRegistry();
    protected EntityRegistry entityRegistry = new ImplEntityRegistry();
    protected BlockEntityRegistry blockEntityRegistry = new ImplBlockEntityRegistry();

    protected PluginManager pluginManager = new ImplPluginManager(this);
    protected ImplResourcePackManager dataPackManager = new ImplResourcePackManager(this);
    protected EventManager eventManager = new ImplEventManager(this);
    protected ImplLevelManager levelManager;

    protected Set<Scheduler> syncedSchedulers = Collections.synchronizedSet(new HashSet<>());
    protected Scheduler scheduler = new Scheduler(this, 1);

    protected final BedrockNetworkServer network = new BedrockNetworkServer(this);
    protected final String ip;
    protected final int port;

    private final Set<PlayerSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private final PlayerDataProvider provider = new NBTPlayerDataProvider(this);

    protected final Logger logger;

    protected int targetTps;
    protected int currentTps;
    protected long currentTick;

    protected final String rootDirectory;
    private volatile boolean running;
    private final CountDownLatch shutdownLatch = new CountDownLatch(1);

    protected int maximumPlayersAllowed;
    protected String motd;

    protected ServerConfig config;


    public ImplServer(String rootDirectory) {
        Server.setInstance(this);

        this.rootDirectory = rootDirectory;
        this.setupFiles();

        this.logger = new ImplLogger("Server");

        this.ip = this.config.getIp();
        this.port = this.config.getPort();

        this.setMotd(this.config.getMotd());
        this.setMaximumPlayerCount(this.config.getMaximumPlayers());

        this.levelManager = new ImplLevelManager(this);
        this.dataPackManager.setPacksRequired(this.config.arePacksForced());

        Runtime.getRuntime().addShutdownHook(new ServerExitListener());
        // TODO: load plugins
    }

    /**
     * Start ticking the Minecraft server.
     * Does not create a new thread and will block the thread that calls this method until shutdown.
     */
    public void boot() throws IOException {
        ServerProtocol.loadVersions();

        this.getResourcePackManager().loadPacks();
        this.setTargetTps(20);

        try {
            this.getNetwork().boot(this.getIp(), this.getPort());
        } catch (InterruptedException | ExecutionException exception) {
            throw new RuntimeException(exception);
        }
        this.running = true;
        this.scheduler.startScheduler();

        this.runTickLoop();
        this.stop();
    }

    private void runTickLoop() {
        int currentTps = 0;
        long nextTpsRecording = 0;

        long nanoSleepTime = 0;

        while (this.running) {
            long idealNanoSleepPerTick = TimeUnit.SECONDS.toNanos(1) / this.targetTps;

            // Figure out how long it took to tick
            long startTickTime = System.nanoTime();
            this.tick();
            this.currentTick++;
            currentTps++;
            long timeTakenToTick = System.nanoTime() - startTickTime;

            // Sleep for the ideal time but take into account the time spent running the tick
            nanoSleepTime += idealNanoSleepPerTick - timeTakenToTick;
            long sleepStart = System.nanoTime();
            try {
                Thread.sleep(Math.max(TimeUnit.NANOSECONDS.toMillis(nanoSleepTime), 0));
            } catch (InterruptedException exception) {
                exception.printStackTrace();
                this.stop();
                return;
            }

            // How long did it actually take to sleep?
            // If we didn't sleep for the correct amount,
            // take that into account for the next sleep by
            // leaving extra/less for the next sleep.
            nanoSleepTime -= System.nanoTime() - sleepStart;

            // Record TPS every second
            if (System.nanoTime() > nextTpsRecording) {
                this.currentTps = currentTps;
                currentTps = 0;
                nextTpsRecording = System.nanoTime() + TimeUnit.SECONDS.toNanos(1);
            }
        }
    }

    private void tick() {
        synchronized (this.sessions) {
            for (PlayerSession session : this.sessions) {
                try {
                    session.processIncomingPackets();
                } catch (Exception exception) {
                    this.getLogger().debug("An exception occurred while processing incoming packets.", exception);
                    if (session.getPlayer() != null) {
                        session.getPlayer().disconnect();
                    } else {
                        session.getConnection().disconnect();
                    }
                }
            }
        }

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
     * The server will stop after the current tick finishes.
     */
    private void stop() {
        this.getLogger().info("Stopping server...");

        for (PlayerSession session : this.sessions) {
            if (session.getPlayer() != null) {
                session.getPlayer().disconnect("Server Stopped");
            } else {
                session.getConnection().disconnect();
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

    public void registerSession(PlayerSession session) {
        session.getPacketHandlerPipeline().addLast(new LoginHandshakePacketHandler(this, session));
        this.sessions.add(session);
    }

    public void unregisterSession(PlayerSession session) {
        this.sessions.remove(session);
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
        synchronized (this.sessions) {
            return this.sessions.stream()
                                .map(PlayerSession::getPlayer)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet());
        }
    }

    @Override
    public Optional<Player> getPlayerByUsername(String username) {
        Optional<Player> exactMatch = this.getPlayerByExactUsername(username);
        if (exactMatch.isPresent()) {
            return exactMatch;
        }

        return this.getPlayers()
                   .stream()
                   .filter(player -> player.getUsername()
                                           .toLowerCase(Locale.ROOT)
                                           .startsWith(username.toLowerCase(Locale.ROOT)))
                   .findAny();
    }

    @Override
    public Optional<Player> getPlayerByExactUsername(String username) {
        return this.getPlayers().stream().filter(player -> player.getUsername().equalsIgnoreCase(username)).findAny();
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
    public long getTick() {
        return this.currentTick;
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

    @Override
    public ServerConfig getConfig() {
        return this.config;
    }

    @Override
    public Scoreboard createScoreboard() {
        return new ImplScoreboard();
    }

    @Override
    public BossBar createBossBar() {
        return new ImplBossBar();
    }

    @Override
    public EntityInventory createInventory(Entity entity, ContainerType containerType) {
        return this.createInventory(entity, containerType, InventoryUtils.getSlotCount(containerType));
    }

    @Override
    public EntityInventory createInventory(Entity entity, ContainerType containerType, int size) {
        return new ImplEntityInventory(entity, containerType, size);
    }

    @Override
    public BlockEntityInventory createInventory(BlockEntity blockEntity, ContainerType containerType) {
        return this.createInventory(blockEntity, containerType, InventoryUtils.getSlotCount(containerType));
    }

    @Override
    public BlockEntityInventory createInventory(BlockEntity blockEntity, ContainerType containerType, int size) {
        return new ImplBlockEntityInventory(blockEntity, containerType, size);
    }

    @Override
    public BlockRegistry getBlockRegistry() {
        return this.blockRegistry;
    }

    @Override
    public ItemRegistry getItemRegistry() {
        return this.itemRegistry;
    }

    @Override
    public EntityRegistry getEntityRegistry() {
        return this.entityRegistry;
    }

    @Override
    public ImplBlockEntityRegistry getBlockEntityRegistry() {
        return (ImplBlockEntityRegistry) this.blockEntityRegistry;
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
    }


    private class ServerExitListener extends Thread {

        @Override
        public void run() {
            if (ImplServer.this.running) {
                ImplServer.this.running = false;
                try {
                    ImplServer.this.shutdownLatch.await();
                } catch (InterruptedException exception) {
                    ImplServer.this.getLogger().error("Exit listener exception", exception);
                }
            }
        }
    }

    public static ImplServer getInstance() {
        return (ImplServer) Server.getInstance();
    }
}
