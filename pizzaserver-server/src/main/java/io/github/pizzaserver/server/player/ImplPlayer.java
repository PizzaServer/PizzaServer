package io.github.pizzaserver.server.player;

import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.data.*;
import com.nukkitx.protocol.bedrock.data.entity.EntityData;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlag;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlags;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import com.nukkitx.protocol.bedrock.packet.*;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.boss.BossBar;
import io.github.pizzaserver.api.entity.data.attributes.Attribute;
import io.github.pizzaserver.api.entity.data.attributes.AttributeType;
import io.github.pizzaserver.api.entity.definition.impl.EntityCowDefinition;
import io.github.pizzaserver.api.entity.definition.impl.EntityHumanDefinition;
import io.github.pizzaserver.api.event.type.entity.EntityDamageEvent;
import io.github.pizzaserver.api.event.type.inventory.InventoryDropItemEvent;
import io.github.pizzaserver.api.event.type.player.PlayerLoginEvent;
import io.github.pizzaserver.api.event.type.player.PlayerRespawnEvent;
import io.github.pizzaserver.api.inventory.Inventory;
import io.github.pizzaserver.api.inventory.TemporaryInventory;
import io.github.pizzaserver.api.item.CreativeRegistry;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.api.network.protocol.PacketHandlerPipeline;
import io.github.pizzaserver.api.network.protocol.version.MinecraftVersion;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.PlayerList;
import io.github.pizzaserver.api.player.data.Device;
import io.github.pizzaserver.api.player.data.Gamemode;
import io.github.pizzaserver.api.player.dialogue.NPCDialogue;
import io.github.pizzaserver.api.player.dialogue.NPCDialogueResponse;
import io.github.pizzaserver.api.player.form.Form;
import io.github.pizzaserver.api.player.form.response.FormResponse;
import io.github.pizzaserver.api.recipe.RecipeRegistry;
import io.github.pizzaserver.api.recipe.type.Recipe;
import io.github.pizzaserver.api.scoreboard.DisplaySlot;
import io.github.pizzaserver.api.scoreboard.Scoreboard;
import io.github.pizzaserver.api.utils.Location;
import io.github.pizzaserver.api.utils.TextMessage;
import io.github.pizzaserver.commons.utils.NumberUtils;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.entity.ImplEntity;
import io.github.pizzaserver.server.entity.ImplEntityHuman;
import io.github.pizzaserver.server.entity.boss.ImplBossBar;
import io.github.pizzaserver.server.inventory.BaseInventory;
import io.github.pizzaserver.server.inventory.ImplPlayerInventory;
import io.github.pizzaserver.server.item.ItemUtils;
import io.github.pizzaserver.server.level.world.ImplWorld;
import io.github.pizzaserver.server.network.data.LoginData;
import io.github.pizzaserver.server.network.protocol.PlayerSession;
import io.github.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.pizzaserver.server.network.protocol.version.BaseMinecraftVersion;
import io.github.pizzaserver.server.player.handlers.AuthInputHandler;
import io.github.pizzaserver.server.player.handlers.InventoryTransactionHandler;
import io.github.pizzaserver.server.player.handlers.PlayerPacketHandler;
import io.github.pizzaserver.server.player.manager.PlayerBlockBreakingManager;
import io.github.pizzaserver.server.player.manager.PlayerChunkManager;
import io.github.pizzaserver.server.player.manager.PlayerPopupManager;
import io.github.pizzaserver.server.player.playerdata.PlayerData;
import io.github.pizzaserver.server.player.playerdata.provider.PlayerDataProvider;
import io.github.pizzaserver.server.recipe.ImplRecipeRegistry;
import io.github.pizzaserver.server.recipe.RecipeUtils;
import io.github.pizzaserver.server.scoreboard.ImplScoreboard;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ImplPlayer extends ImplEntityHuman implements Player {

    protected final ImplServer server;
    protected final PlayerSession session;
    protected boolean locallyInitialized;
    protected boolean autoSave = true;

    protected final MinecraftVersion version;
    protected final Device device;
    protected final String xuid;
    protected final UUID uuid;
    protected final String username;
    protected final String languageCode;

    protected final PlayerList playerList = new ImplPlayerList(this);
    protected final Set<BossBar> bossBars = new HashSet<>();
    protected final Map<DisplaySlot, Scoreboard> scoreboards = new HashMap<>();

    protected final PlayerPopupManager popupManager = new PlayerPopupManager(this);

    protected final PlayerChunkManager chunkManager = new PlayerChunkManager(this);
    protected Dimension dimensionTransferScreen = null;

    protected Inventory openInventory = null;

    protected Gamemode gamemode;
    protected ImplAdventureSettings adventureSettings = new ImplAdventureSettings(this);

    protected final PlayerBlockBreakingManager breakingManager = new PlayerBlockBreakingManager(this);


    public ImplPlayer(ImplServer server, PlayerSession session, LoginData loginData) {
        super(server.getEntityRegistry().getDefinition(EntityHumanDefinition.ID));
        this.server = server;
        this.session = session;

        this.version = session.getVersion();
        this.device = loginData.getDevice();
        this.xuid = loginData.getXUID();
        this.uuid = loginData.getUUID();
        this.username = loginData.getUsername();
        this.languageCode = loginData.getLanguageCode();
        this.skin = loginData.getSkin();
        this.inventory = new ImplPlayerInventory(this);

        this.physicsEngine.setPositionUpdate(false);

        this.setDisplayName(this.getUsername());

        // Players will die at any health lower than 0.5
        this.getAttribute(AttributeType.HEALTH).setMinimumValue(0.5f);
    }

    /**
     * Initialize the player to be ready to spawn in.
     */
    public void initialize() {
        this.getServer().getScheduler().prepareTask(() -> {
            // Load player data
            PlayerData data;
            try {
                data = this.getSavedData().orElse(null);
            } catch (IOException exception) {
                this.getServer().getLogger().error("Failed to retrieve data of " + this.getUUID(), exception);
                this.disconnect();
                return;
            }
            if (data == null) {
                String defaultWorldName = this.getServer().getConfig().getDefaultWorldName();
                ImplWorld defaultWorld = this.getServer().getLevelManager().getLevelDimension(defaultWorldName, Dimension.OVERWORLD);
                if (defaultWorld == null) {
                    this.disconnect();
                    this.getServer().getLogger().error("Failed to find a world by the name of " + defaultWorldName);
                    return;
                }
                data = defaultWorld.getDefaultPlayerData();
            }

            this.completePlayerInitialization(data);
        }).setAsynchronous(true).schedule();
    }

    /**
     * Applies player data on the main thread and sends remaining packets to spawn player.
     * @param data player data
     */
    private void completePlayerInitialization(PlayerData data) {
        this.getServer().getScheduler().prepareTask(() -> {
            if (this.server.getPlayerCount() >= this.server.getMaximumPlayerCount()) {
                PlayStatusPacket playStatusPacket = new PlayStatusPacket();
                playStatusPacket.setStatus(PlayStatusPacket.Status.FAILED_SERVER_FULL_SUB_CLIENT);
                this.session.getConnection().sendPacket(playStatusPacket);
                return;
            }

            // Disconnect the player's other session if they're logged in on another device
            for (Player player : this.getServer().getPlayers()) {
                if (player.getXUID().equals(this.getXUID())) {
                    player.disconnect();
                }
            }

            this.getServer().getEntityRegistry().getDefinition(EntityHumanDefinition.ID).onCreation(this);

            // Apply player data
            this.setPitch(data.getPitch());
            this.setYaw(data.getYaw());
            this.setHeadYaw(data.getYaw());
            this.setGamemode(data.getGamemode());

            // Get their spawn location
            World world = this.server.getLevelManager().getLevelDimension(data.getLevelName(), data.getDimension());
            Vector3f rotation = Vector3f.from(data.getPitch(), data.getYaw(), data.getYaw());
            Location spawnLocation;
            if (world == null) { // Was the world deleted? Set it to the default world if so
                String defaultWorldName = this.getServer().getConfig().getDefaultWorldName();
                world = this.getServer().getLevelManager().getLevelDimension(defaultWorldName, Dimension.OVERWORLD);
                spawnLocation = new Location(world, world.getSpawnCoordinates(), rotation);
            } else {
                spawnLocation = new Location(world, data.getPosition(), rotation);
            }

            PlayerLoginEvent playerLoginEvent = new PlayerLoginEvent(this, spawnLocation);
            this.getServer().getEventManager().call(playerLoginEvent);
            if (playerLoginEvent.isCancelled()) {
                this.disconnect();
                return;
            }

            // Send remaining packets to spawn player
            SyncedPlayerMovementSettings movementSettings = new SyncedPlayerMovementSettings();
            movementSettings.setMovementMode(AuthoritativeMovementMode.SERVER_WITH_REWIND);
            movementSettings.setRewindHistorySize(100);
            movementSettings.setServerAuthoritativeBlockBreaking(true);

            StartGamePacket startGamePacket = new StartGamePacket();
            startGamePacket.setUniqueEntityId(this.getId());
            startGamePacket.setRuntimeEntityId(this.getId());
            startGamePacket.setPlayerGameType(GameType.from(this.getGamemode().ordinal()));
            startGamePacket.setPlayerPosition(spawnLocation.toVector3f().add(0, this.getEyeHeight(), 0));
            startGamePacket.setRotation(Vector2f.from(spawnLocation.getPitch(), spawnLocation.getYaw()));
            startGamePacket.setDimensionId(world.getDimension().ordinal());
            startGamePacket.setLevelGameType(GameType.SURVIVAL);
            startGamePacket.setDifficulty(world.getLevel().getDifficulty().ordinal());
            startGamePacket.setDefaultSpawn(world.getSpawnCoordinates());
            startGamePacket.setDayCycleStopTime(world.getTime());
            startGamePacket.setLevelName(this.getServer().getMotd());
            startGamePacket.setLevelId(Base64.getEncoder().encodeToString(startGamePacket.getLevelName().getBytes(StandardCharsets.UTF_8)));
            startGamePacket.setGeneratorId(1);
            startGamePacket.setDefaultPlayerPermission(PlayerPermission.MEMBER);
            startGamePacket.setServerChunkTickRange(this.getServer().getConfig().getChunkRadius());
            startGamePacket.setVanillaVersion(ServerProtocol.LATEST_GAME_VERSION);
            startGamePacket.setPremiumWorldTemplateId("");
            startGamePacket.setInventoriesServerAuthoritative(true);
            startGamePacket.getExperiments().add(new ExperimentData("data_driven_items", true));
            startGamePacket.getGamerules().add(new GameRuleData<>("showcoordinates", true));
            startGamePacket.setItemEntries(((BaseMinecraftVersion) this.getVersion()).getItemEntries());
            startGamePacket.getBlockProperties().addAll(((BaseMinecraftVersion) this.getVersion()).getCustomBlockProperties());
            startGamePacket.setPlayerMovementSettings(movementSettings);
            startGamePacket.setCommandsEnabled(true);
            startGamePacket.setMultiplayerGame(true);
            startGamePacket.setBroadcastingToLan(true);
            startGamePacket.setMultiplayerCorrelationId(UUID.randomUUID().toString());
            startGamePacket.setXblBroadcastMode(GamePublishSetting.PUBLIC);
            startGamePacket.setPlatformBroadcastMode(GamePublishSetting.PUBLIC);
            startGamePacket.setCurrentTick(this.getServer().getTick());
            startGamePacket.setServerEngine("");
            this.sendPacket(startGamePacket);

            // Send item components for custom items
            ItemComponentPacket itemComponentPacket = new ItemComponentPacket();
            itemComponentPacket.getItems().addAll(((BaseMinecraftVersion) this.getVersion()).getItemComponents());
            this.sendPacket(itemComponentPacket);

            CreativeContentPacket creativeContentPacket = new CreativeContentPacket();
            creativeContentPacket.setContents(CreativeRegistry.getInstance()
                    .getItems().stream()
                    .map(item -> ItemUtils.serializeForNetwork(item, this.getVersion()))
                    .toArray(ItemData[]::new));
            this.sendPacket(creativeContentPacket);

            CraftingDataPacket craftingDataPacket = new CraftingDataPacket();
            craftingDataPacket.getCraftingData().addAll(new ArrayList<>() {
                {
                    for (Recipe recipe : ((ImplRecipeRegistry) RecipeRegistry.getInstance()).getRecipes()) {
                        this.add(RecipeUtils.serializeForNetwork(recipe, ImplPlayer.this.getVersion()));
                    }
                }
            });
            this.sendPacket(craftingDataPacket);

            BiomeDefinitionListPacket biomeDefinitionPacket = new BiomeDefinitionListPacket();
            biomeDefinitionPacket.setDefinitions(((BaseMinecraftVersion) this.getVersion()).getBiomeDefinitions());
            this.sendPacket(biomeDefinitionPacket);

            AvailableEntityIdentifiersPacket availableEntityIdentifiersPacket = new AvailableEntityIdentifiersPacket();
            availableEntityIdentifiersPacket.setIdentifiers(((BaseMinecraftVersion) this.getVersion()).getEntityIdentifiers());
            this.sendPacket(availableEntityIdentifiersPacket);


            // Sent the full player list to this player
            List<PlayerList.Entry> entries = this.getServer().getPlayers().stream()
                    .filter(otherPlayer -> !otherPlayer.isHiddenFrom(this))
                    .map(Player::getPlayerListEntry)
                    .collect(Collectors.toList());
            this.getPlayerList().addEntries(entries);

            spawnLocation.getWorld().addEntity(this, spawnLocation.toVector3f());
            this.session.getConnection().getHardcodedBlockingId().set(this.version.getItemRuntimeId(ItemID.SHIELD));
            this.getPacketHandlerPipeline().addLast(new PlayerPacketHandler(this));
            this.getPacketHandlerPipeline().addLast(new InventoryTransactionHandler(this));
            this.getPacketHandlerPipeline().addLast(new AuthInputHandler(this));
            this.session.setPlayer(this);

            PlayStatusPacket playStatusPacket = new PlayStatusPacket();
            playStatusPacket.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);
            this.sendPacket(playStatusPacket);
        }).schedule();
    }

    @Override
    public MinecraftVersion getVersion() {
        return this.version;
    }

    @Override
    public Device getDevice() {
        return this.device;
    }

    @Override
    public String getXUID() {
        return this.xuid;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getLanguageCode() {
        return this.languageCode;
    }

    @Override
    public boolean isLocallyInitialized() {
        return this.locallyInitialized;
    }

    @Override
    public Gamemode getGamemode() {
        return this.gamemode;
    }

    @Override
    public void setGamemode(Gamemode gamemode) {
        if (gamemode != this.gamemode) {
            boolean wasCreativeMode = this.gamemode == Gamemode.CREATIVE;

            this.gamemode = gamemode;

            if (this.hasSpawned()) {
                SetPlayerGameTypePacket setPlayerGamemodePacket = new SetPlayerGameTypePacket();
                setPlayerGamemodePacket.setGamemode(gamemode.ordinal());
                this.sendPacket(setPlayerGamemodePacket);
            }

            if (wasCreativeMode) {
                this.getAdventureSettings().setCanFly(false);
            } else if (this.gamemode == Gamemode.CREATIVE) {
                this.getAdventureSettings().setCanFly(true);
            }
        }
    }

    @Override
    public boolean isCreativeMode() {
        return this.getGamemode() == Gamemode.CREATIVE;
    }

    @Override
    public boolean isAdventureMode() {
        return this.getGamemode() == Gamemode.ADVENTURE;
    }

    @Override
    public boolean isSurvivalMode() {
        return this.getGamemode() == Gamemode.SURVIVAL;
    }

    @Override
    public ImplAdventureSettings getAdventureSettings() {
        return this.adventureSettings;
    }

    public void onLocallyInitialized() {
        this.locallyInitialized = true;

        for (Player player : this.getServer().getPlayers()) {
            if (!this.isHiddenFrom(player) && !player.equals(this)) {
                player.getPlayerList().addEntry(this.getPlayerListEntry());
            }
        }

        this.getChunkManager().onLocallyInitialized();
    }

    public PlayerBlockBreakingManager getBlockBreakingManager() {
        return this.breakingManager;
    }

    @Override
    public boolean canReach(Entity entity) {
        return this.canReach(entity.getLocation().toVector3f(), this.isCreativeMode() ? 9 : 6);
    }

    @Override
    public boolean canReach(BlockEntity<? extends Block> blockEntity) {
        return this.canReach(blockEntity.getBlock().getLocation().toVector3f(), this.isCreativeMode() ? 13 : 7);
    }

    @Override
    public boolean canReach(Block block) {
        return this.canReach(block.getLocation().toVector3f(), this.isCreativeMode() ? 13 : 7);
    }

    public boolean canReach(Vector3i vector3, float maxDistance) {
        return this.canReach(Vector3f.from(vector3.getX(), vector3.getY(), vector3.getZ()), maxDistance);
    }

    public boolean canReach(Vector3f vector3, float maxDistance) {
        Vector3f position = this.getLocation().toVector3f().add(0, this.getEyeHeight(), 0);

        // Distance check
        double distance = position.distance(vector3);
        if (distance > maxDistance) {
            return false;
        }

        // Direction check
        Vector3f playerDirectionVector = this.getDirectionVector();
        Vector3f targetDirectionVector = vector3.sub(this.getLocation().toVector3f().add(0, this.getEyeHeight(), 0)).normalize();

        // Must be in same direction ( > 0) but we allow a little leeway to account for attacking an entity in the same position as you
        return playerDirectionVector.dot(targetDirectionVector) > -1;
    }

    @Override
    public void kill() {
        if (!this.getGamemode().equals(Gamemode.CREATIVE)) {
            super.kill();
        }
    }

    @Override
    public void hurt(float damage) {
        if (!this.getGamemode().equals(Gamemode.CREATIVE)) {
            super.hurt(damage);
        }
    }

    @Override
    public boolean damage(EntityDamageEvent event) {
        if (this.getGamemode().equals(Gamemode.CREATIVE)) {
            return false;
        } else {
            return super.damage(event);
        }
    }

    public void respawn() {
        this.deathAnimationTicks = -1;
        this.fireTicks = 0;
        this.noHitTicks = 0;
        this.lastDamageEvent = null;
        this.setAI(true);
        this.setAirSupplyTicks(this.getMaxAirSupplyTicks());
        this.setSwimming(false);

        Location respawnLocation = this.getSpawn();
        if (respawnLocation.getWorld().getDimension() != this.getWorld().getDimension()) {
            this.setDimensionTransferScreen(respawnLocation.getWorld().getDimension());
        }

        this.setHealth(this.getMaxHealth());
        this.setFoodLevel(20);

        PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(this, respawnLocation);
        this.getServer().getEventManager().call(respawnEvent);

        respawnLocation.getWorld().addEntity(this, respawnEvent.getLocation().toVector3f());
        this.teleport(respawnEvent.getLocation());
    }

    public ImplServer getServer() {
        return this.server;
    }

    @Override
    public ImplPlayerInventory getInventory() {
        return (ImplPlayerInventory) this.inventory;
    }

    @Override
    public Optional<Inventory> getOpenInventory() {
        return Optional.ofNullable(this.openInventory);
    }

    @Override
    public boolean closeOpenInventory() {
        Optional<Inventory> openInventory = this.getOpenInventory();
        if (openInventory.isPresent()) {
            if (!((BaseInventory) openInventory.get()).closeFor(this)) {
                return false;
            } else {
                // Drop items from open inventory if required.
                this.handleClosingTemporaryInventory(this.getInventory().getCraftingGrid());
                if (openInventory.get() instanceof TemporaryInventory openTempInventory) {
                    this.handleClosingTemporaryInventory(openTempInventory);
                }

                this.openInventory = null;
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Moves all items in the temporary inventory to the player's inventory or to the ground.
     * Normally this is handled by the client. but in the case scenario of malicious clients,
     * they could keep the items in their temporary inventory.
     */
    private void handleClosingTemporaryInventory(TemporaryInventory inventory) {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            Item item = inventory.getSlot(slot);

            Optional<Item> excess = this.getInventory().addItem(item);
            if (excess.isPresent()) {
                // The default behaviour is to drop the item if we cannot add it to our inventory.
                if (this.tryDroppingItem(inventory, excess.get())) {
                    inventory.setSlot(slot, null);
                }
            } else {
                inventory.setSlot(slot, null);
            }
        }
    }

    /**
     * Attempts to drop an item from the player's inventory. (calls the InventoryDropItemEvent)
     * @param inventory the inventory of the item being dropped
     * @param item the item being dropped
     * @return if the item was dropped.
     */
    public boolean tryDroppingItem(Inventory inventory, Item item) {
        InventoryDropItemEvent dropItemEvent = new InventoryDropItemEvent(inventory, this, item);
        if (!dropItemEvent.isCancelled()) {
            Item droppedItem = dropItemEvent.getDrop();

            EntityItem itemEntity = EntityRegistry.getInstance().getItemEntity(droppedItem);
            itemEntity.setPickupDelay(40);
            this.getWorld().addItemEntity(itemEntity, this.getLocation().toVector3f().add(0, 1.3f, 0), this.getDirectionVector().mul(0.25f, 0.6f, 0.25f));

            return true;
        }

        return false;
    }

    @Override
    public boolean openInventory(Inventory inventory) {
        this.closeOpenInventory();
        if (((BaseInventory) inventory).openFor(this)) {
            this.openInventory = inventory;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public PlayerList getPlayerList() {
        return this.playerList;
    }

    /**
     * Fetch the SAVED player data from the {@link PlayerDataProvider} if any exists.
     * @return saved player data
     * @throws IOException if an exception occurred while reading the data
     */
    public Optional<PlayerData> getSavedData() throws IOException {
        return this.getServer().getPlayerProvider()
                .load(this.getUUID());
    }

    @Override
    public boolean save() {
        if (this.hasSpawned()) {
            PlayerData playerData = new PlayerData.Builder()
                    .setLevelName(this.getLevel().getProvider().getFile().getName())
                    .setDimension(this.getLocation().getWorld().getDimension())
                    .setGamemode(this.getGamemode())
                    .setPosition(this.getLocation().toVector3f())
                    .setPitch(this.getPitch())
                    .setYaw(this.getYaw())
                    .build();
            try {
                this.getServer().getPlayerProvider().save(this.getUUID(), playerData);
                return true;
            } catch (IOException exception) {
                this.getServer().getLogger().error("Failed to save player " + this.getUUID(), exception);
            }
        }
        return false;
    }

    /**
     * Called when the server registers that the player is disconnected.
     * It cleans up data for this player
     */
    public void onDisconnect() {
        if (this.hasSpawned()) {
            this.closeOpenInventory();

            if (this.canAutoSave()) {
                this.getServer().getScheduler().prepareTask(() -> {
                    this.save();
                    this.getServer().getScheduler().prepareTask(this::despawn).schedule();
                }).setAsynchronous(true).schedule();
            } else {
                this.despawn();
            }

            // remove the player from the player list of others
            for (Player player : this.getServer().getPlayers()) {
                player.getPlayerList().removeEntry(this.getPlayerListEntry());
            }
        }
    }

    private void sendAttribute(Attribute attribute) {
        this.sendAttributes(Collections.singleton(attribute));
    }

    private void sendAttributes() {
        this.sendAttributes(this.attributes.getAttributes());
    }

    private void sendAttributes(Set<Attribute> attributes) {
        if (this.hasSpawned()) {
            UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
            updateAttributesPacket.setRuntimeEntityId(this.getId());
            updateAttributesPacket.setAttributes(attributes.stream().map(Attribute::serialize).collect(Collectors.toList()));
            this.sendPacket(updateAttributesPacket);
        }
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        this.sendAttribute(this.getAttribute(AttributeType.HEALTH));
    }

    @Override
    public void setMaxHealth(float maxHealth) {
        super.setMaxHealth(maxHealth);
        this.sendAttribute(this.getAttribute(AttributeType.HEALTH));
    }

    @Override
    public void setAbsorption(float absorption) {
        super.setAbsorption(absorption);
        this.sendAttribute(this.getAttribute(AttributeType.ABSORPTION));
    }

    @Override
    public void setMaxAbsorption(float maxAbsorption) {
        super.setMaxAbsorption(maxAbsorption);
        this.sendAttribute(this.getAttribute(AttributeType.ABSORPTION));
    }

    @Override
    public void setMovementSpeed(float movementSpeed) {
        super.setMovementSpeed(movementSpeed);
        this.sendAttribute(this.getAttribute(AttributeType.MOVEMENT_SPEED));
    }

    @Override
    public float getFoodLevel() {
        Attribute attribute = this.getAttribute(AttributeType.FOOD);
        return attribute.getCurrentValue();
    }

    @Override
    public void setFoodLevel(float foodLevel) {
        Attribute attribute = this.getAttribute(AttributeType.FOOD);
        attribute.setCurrentValue(Math.max(attribute.getMinimumValue(), foodLevel));
        this.sendAttribute(attribute);
    }

    @Override
    public float getSaturationLevel() {
        Attribute attribute = this.getAttribute(AttributeType.SATURATION);
        return attribute.getCurrentValue();
    }

    @Override
    public void setSaturationLevel(float saturationLevel) {
        Attribute attribute = this.getAttribute(AttributeType.SATURATION);
        attribute.setCurrentValue(Math.max(attribute.getMinimumValue(), saturationLevel));
        this.sendAttribute(attribute);
    }

    @Override
    public float getExperience() {
        Attribute attribute = this.getAttribute(AttributeType.EXPERIENCE);
        return attribute.getCurrentValue();
    }

    @Override
    public void setExperience(float experience) {
        Attribute attribute = this.getAttribute(AttributeType.EXPERIENCE);
        attribute.setCurrentValue(Math.max(attribute.getMinimumValue(), experience));
        this.sendAttribute(attribute);
    }

    @Override
    public int getExperienceLevel() {
        Attribute attribute = this.getAttribute(AttributeType.EXPERIENCE_LEVEL);
        return (int) attribute.getCurrentValue();
    }

    @Override
    public void setExperienceLevel(int experienceLevel) {
        Attribute attribute = this.getAttribute(AttributeType.EXPERIENCE_LEVEL);
        attribute.setCurrentValue(Math.max(attribute.getMinimumValue(), experienceLevel));
        this.sendAttribute(attribute);
    }

    @Override
    public void teleport(World world, float x, float y, float z, float pitch, float yaw, float headYaw) {
        super.teleport(world, x, y, z, pitch, yaw, headYaw);

        MovePlayerPacket teleportPacket = new MovePlayerPacket();
        teleportPacket.setRuntimeEntityId(this.getId());
        teleportPacket.setPosition(Vector3f.from(x, y + this.getEyeHeight(), z));
        teleportPacket.setRotation(Vector3f.from(pitch, yaw, headYaw));
        teleportPacket.setMode(MovePlayerPacket.Mode.TELEPORT);
        teleportPacket.setTeleportationCause(MovePlayerPacket.TeleportationCause.UNKNOWN);
        this.sendPacket(teleportPacket);
    }

    @Override
    public void teleport(World world, float x, float y, float z, float pitch, float yaw, float headYaw, Dimension transferDimension) {
        World oldWorld = this.getWorld();
        this.teleport(world, x, y, z, pitch, yaw, headYaw);

        if (!oldWorld.getDimension().equals(transferDimension)) {
            this.setDimensionTransferScreen(transferDimension);
        }
    }

    @Override
    public Location getSpawn() {
        if (this.getHome().isPresent()) {
            return new Location(this.getWorld(), this.getHome().get().toLocation().toVector3f().add(0, this.getEyeHeight(), 0));
        } else {
            World world = this.getServer().getLevelManager().getDefaultLevel().getDimension(Dimension.OVERWORLD);
            return new Location(world, world.getSpawnCoordinates().add(0, this.getEyeHeight(), 0));
        }
    }

    /**
     * Returns the current dimension transfer screen being shown to the player.
     * @return dimension transfer screen
     */
    public Optional<Dimension> getDimensionTransferScreen() {
        return Optional.ofNullable(this.dimensionTransferScreen);
    }

    /**
     * Send a dimension change packet.
     * @param dimension dimension to send the transfer screen of
     */
    public void setDimensionTransferScreen(Dimension dimension) {
        this.dimensionTransferScreen = dimension;
        if (dimension != null) {
            ChangeDimensionPacket changeDimensionPacket = new ChangeDimensionPacket();
            changeDimensionPacket.setDimension(dimension.ordinal());
            changeDimensionPacket.setPosition(this.getLocation().toVector3f().add(0, this.getEyeHeight(), 0));
            if (!this.isAlive()) {
                changeDimensionPacket.setRespawn(true);
            }
            this.sendPacket(changeDimensionPacket);
            this.getChunkManager().onDimensionTransfer();
            this.getPopupManager().closeAllForms();
        }
    }

    @Override
    public void sendMessage(TextMessage message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setType(message.getType());
        textPacket.setMessage(message.getMessage());
        textPacket.setNeedsTranslation(message.requiresTranslation());
        textPacket.setSourceName(message.getSourceName());
        textPacket.setParameters(message.getParameters());
        textPacket.setXuid(message.getXuid());
        textPacket.setPlatformChatId(message.getPlatformChatId());
        this.sendPacket(textPacket);
    }

    @Override
    public void sendMessage(String message) {
        this.sendMessage(new TextMessage.Builder()
                .setType(TextPacket.Type.RAW)
                .setMessage(message)
                .build());
    }

    @Override
    public void sendPlayerMessage(Player sender, String message) {
        this.sendMessage(new TextMessage.Builder()
                        .setType(TextPacket.Type.CHAT)
                        .setSourceName(sender.getUsername())
                        .setMessage(message)
                        .setXUID(sender.getXUID())
                        .build());
    }

    public PlayerChunkManager getChunkManager() {
        return this.chunkManager;
    }

    @Override
    public int getChunkRadius() {
        return this.getChunkManager().getChunkRadius();
    }

    @Override
    public void setChunkRadius(int radius) {
        this.getChunkManager().setChunkRadius(radius);
    }

    @Override
    public void showBossBar(BossBar bossbar) {
        ImplBossBar implBossbar = (ImplBossBar) bossbar;
        if (!implBossbar.isDummy()) {
            throw new IllegalArgumentException("The boss bar is already assigned to an entity.");
        }
        if (implBossbar.getEntityId() == -1) {
            implBossbar.setEntityId(ImplEntity.ID++);
        }

        if (this.bossBars.add(implBossbar)) {
            AddEntityPacket dummyEntityPacket = new AddEntityPacket();
            dummyEntityPacket.setIdentifier(EntityCowDefinition.ID);
            dummyEntityPacket.setUniqueEntityId(implBossbar.getEntityId());
            dummyEntityPacket.setRuntimeEntityId(implBossbar.getEntityId());
            dummyEntityPacket.setMotion(Vector3f.ZERO);
            dummyEntityPacket.setRotation(Vector3f.ZERO);
            dummyEntityPacket.setPosition(this.getLocation().toVector3f());

            dummyEntityPacket.getMetadata().putFloat(EntityData.BOUNDING_BOX_HEIGHT, 0);
            dummyEntityPacket.getMetadata().putFloat(EntityData.BOUNDING_BOX_WIDTH, 0);
            dummyEntityPacket.getMetadata().putFloat(EntityData.SCALE, 0);
            EntityFlags flags = dummyEntityPacket.getMetadata().getOrCreateFlags();
            flags.setFlag(EntityFlag.INVISIBLE, true);
            dummyEntityPacket.getMetadata().putFlags(flags);

            this.sendPacket(dummyEntityPacket);
            implBossbar.spawnTo(this);
        }
    }

    @Override
    public boolean hideBossBar(BossBar bossbar) {
        if (this.bossBars.remove(bossbar) && ((ImplBossBar) bossbar).despawnFrom(this)) {
            RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
            removeEntityPacket.setUniqueEntityId(((ImplBossBar) bossbar).getEntityId());
            this.sendPacket(removeEntityPacket);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Scoreboard> getScoreboard(DisplaySlot displaySlot) {
        return Optional.ofNullable(this.scoreboards.getOrDefault(displaySlot, null));
    }

    @Override
    public void setScoreboard(DisplaySlot displaySlot, Scoreboard scoreboard) {
        Optional<Scoreboard> activeScoreboard = this.getScoreboard(displaySlot);
        if (activeScoreboard.isPresent() && !activeScoreboard.get().equals(scoreboard)) {
            ((ImplScoreboard) activeScoreboard.get()).despawnFrom(this);
        }
        this.scoreboards.put(displaySlot, scoreboard);

        if (scoreboard != null) {
            this.ensureUniqueSlotForScoreboard(displaySlot, scoreboard);
            ((ImplScoreboard) scoreboard).spawnTo(this, displaySlot);
        }
    }

    public PlayerPopupManager getPopupManager() {
        return this.popupManager;
    }

    @Override
    public void showForm(Form form, Consumer<FormResponse<? extends Form>> callback) {
        this.getPopupManager().showForm(form, callback);
    }

    @Override
    public void showDialogue(NPCDialogue dialogue, Consumer<NPCDialogueResponse> callback) {
        this.getPopupManager().showDialogue(dialogue, callback);
    }

    @Override
    public void hideDialogue() {
        this.getPopupManager().hideDialogue();
    }

    @Override
    public PacketHandlerPipeline getPacketHandlerPipeline() {
        return this.session.getPacketHandlerPipeline();
    }

    /**
     * Ensure that only a scoreboard does not occupy another display slot already.
     * If it does, it should despawn that one.
     * @param displaySlot display slot of the new scoreboard being set
     * @param scoreboard scoreboard to check against
     */
    private void ensureUniqueSlotForScoreboard(DisplaySlot displaySlot, Scoreboard scoreboard) {
        Optional<Scoreboard> listScoreboard = this.getScoreboard(DisplaySlot.LIST);
        Optional<Scoreboard> sidebarScoreboard = this.getScoreboard(DisplaySlot.SIDEBAR);
        Optional<Scoreboard> belowNameScoreboard = this.getScoreboard(DisplaySlot.BELOW_NAME);
        switch (displaySlot) {
            case LIST:
                if (sidebarScoreboard.isPresent() && sidebarScoreboard.get().equals(scoreboard)) {
                    ((ImplScoreboard) sidebarScoreboard.get()).despawnFrom(this);
                    this.scoreboards.remove(DisplaySlot.SIDEBAR);
                } else if (belowNameScoreboard.isPresent() && belowNameScoreboard.get().equals(scoreboard)) {
                    ((ImplScoreboard) belowNameScoreboard.get()).despawnFrom(this);
                    this.scoreboards.remove(DisplaySlot.BELOW_NAME);
                }
                break;
            case SIDEBAR:
                if (listScoreboard.isPresent() && listScoreboard.get().equals(scoreboard)) {
                    ((ImplScoreboard) listScoreboard.get()).despawnFrom(this);
                    this.scoreboards.remove(DisplaySlot.LIST);
                } else if (belowNameScoreboard.isPresent() && belowNameScoreboard.get().equals(scoreboard)) {
                    ((ImplScoreboard) belowNameScoreboard.get()).despawnFrom(this);
                    this.scoreboards.remove(DisplaySlot.BELOW_NAME);
                }
                break;
            case BELOW_NAME:
                if (listScoreboard.isPresent() && listScoreboard.get().equals(scoreboard)) {
                    ((ImplScoreboard) listScoreboard.get()).despawnFrom(this);
                    this.scoreboards.remove(DisplaySlot.LIST);
                } else if (sidebarScoreboard.isPresent() && sidebarScoreboard.get().equals(scoreboard)) {
                    ((ImplScoreboard) sidebarScoreboard.get()).despawnFrom(this);
                    this.scoreboards.remove(DisplaySlot.SIDEBAR);
                }
                break;
        }
    }

    @Override
    public boolean isConnected() {
        return !this.session.getConnection().isClosed();
    }

    @Override
    public long getPing() {
        return this.session.getConnection().getLatency();
    }

    @Override
    public void sendPacket(BedrockPacket packet) {
        if (!this.session.getConnection().isClosed()) {
            this.session.getConnection().sendPacket(packet);
        }
    }

    @Override
    public void sendPacketImmediately(BedrockPacket packet) {
        if (!this.session.getConnection().isClosed()) {
            this.session.getConnection().sendPacketImmediately(packet);
        }
    }

    @Override
    public void disconnect() {
        this.session.getConnection().disconnect();
    }

    @Override
    public void disconnect(String reason) {
        this.session.getConnection().disconnect(reason);
    }

    @Override
    public void setAutoSave(boolean allowAutoSaving) {
        this.autoSave = allowAutoSaving;
    }

    @Override
    public boolean canAutoSave() {
        return this.autoSave;
    }

    @Override
    public void tick() {
        if (this.metaDataUpdate) {
            SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
            setEntityDataPacket.setRuntimeEntityId(this.getId());
            setEntityDataPacket.getMetadata().putAll(this.getMetaData().serialize());
            this.sendPacket(setEntityDataPacket);
        }

        this.getBlockBreakingManager().tick();

        if (!NumberUtils.isNearlyEqual(this.getHealth(), this.getMaxHealth()) && this.getFoodLevel() >= 18 && this.ticks % 80 == 0) {
            this.setHealth(this.getHealth() + 1);
        }

        boolean shouldCloseOpenInventory = this.getOpenInventory().filter(inventory -> ((BaseInventory) inventory).shouldBeClosedFor(this)).isPresent();
        if (shouldCloseOpenInventory) {
            this.closeOpenInventory();
        }

        super.tick();
    }

    @Override
    public void moveTo(float x, float y, float z, float pitch, float yaw, float headYaw) {
        Location oldLocation = new Location(this.world, Vector3f.from(this.x, this.y, this.z), Vector3f.from(this.pitch, this.yaw, this.headYaw));
        super.moveTo(x, y, z, pitch, yaw, headYaw);

        if (!oldLocation.getChunk().equals(this.getChunk())) {
            this.getChunkManager().onChunkChange(oldLocation);
        }
    }

    @Override
    public void onSpawned() {
        super.onSpawned();
        this.getChunkManager().onSpawned();

        this.getInventory().sendSlots(this);
        this.getMetaData().putFlag(EntityFlag.HAS_GRAVITY, true)
                .putFlag(EntityFlag.BREATHING, true)
                .putFlag(EntityFlag.CAN_CLIMB, true);
        this.sendAttributes();
        this.getAdventureSettings().send();

        SetTimePacket setTimePacket = new SetTimePacket();
        setTimePacket.setTime(this.getWorld().getTime());
        this.sendPacket(setTimePacket);
    }

    @Override
    public void onDespawned() {
        super.onDespawned();
        this.getChunkManager().onDespawn();
    }

    @Override
    public void showTo(Player player) {
        if (this.isHiddenFrom(player)) {
            super.showTo(player);
            if (player.hasSpawned()) {  // we only need to add the entry if we were spawned
                player.getPlayerList().addEntry(this.getPlayerListEntry());
            }
        }
    }

    @Override
    public void hideFrom(Player player) {
        if (!this.isHiddenFrom(player)) {
            super.hideFrom(player);
            if (player.hasSpawned()) {  // we only need to remove the entry if we were spawned
                player.getPlayerList().removeEntry(this.getPlayerListEntry());
            }
        }
    }

}
