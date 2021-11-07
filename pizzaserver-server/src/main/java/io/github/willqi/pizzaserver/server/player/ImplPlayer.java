package io.github.willqi.pizzaserver.server.player;

import io.github.willqi.pizzaserver.api.entity.EntityRegistry;
import io.github.willqi.pizzaserver.api.entity.inventory.Inventory;
import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.entity.definition.impl.HumanEntityDefinition;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlagCategory;
import io.github.willqi.pizzaserver.api.event.type.block.BlockStopBreakEvent;
import io.github.willqi.pizzaserver.api.event.type.entity.EntityDamageEvent;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.level.world.data.Dimension;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.player.AdventureSettings;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.player.PlayerList;
import io.github.willqi.pizzaserver.api.entity.attributes.Attribute;
import io.github.willqi.pizzaserver.api.player.data.Gamemode;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.utils.TextMessage;
import io.github.willqi.pizzaserver.api.utils.TextType;
import io.github.willqi.pizzaserver.commons.utils.NumberUtils;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.entity.ImplHumanEntity;
import io.github.willqi.pizzaserver.server.entity.inventory.BaseInventory;
import io.github.willqi.pizzaserver.server.entity.inventory.ImplPlayerInventory;
import io.github.willqi.pizzaserver.server.network.BedrockClientSession;
import io.github.willqi.pizzaserver.api.network.protocol.packets.*;
import io.github.willqi.pizzaserver.api.entity.attributes.AttributeType;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.api.player.data.Device;
import io.github.willqi.pizzaserver.server.player.playerdata.PlayerData;

import java.util.*;
import java.io.IOException;

public class ImplPlayer extends ImplHumanEntity implements Player {

    protected final ImplServer server;
    protected final BedrockClientSession session;
    protected boolean locallyInitialized;
    protected boolean autoSave = true;

    protected final BaseMinecraftVersion version;
    protected final Device device;
    protected final String xuid;
    protected final UUID uuid;
    protected final String username;
    protected final String languageCode;

    protected int regenerationTicks = 80;

    protected final PlayerList playerList = new ImplPlayerList(this);

    protected final PlayerChunkManager chunkManager = new PlayerChunkManager(this);
    protected Dimension dimensionTransferScreen = null;

    protected Inventory openInventory = null;

    protected Gamemode gamemode;
    protected ImplAdventureSettings adventureSettings = new ImplAdventureSettings();

    protected final BreakingData breakingData = new BreakingData(this);


    public ImplPlayer(ImplServer server, BedrockClientSession session, LoginPacket loginPacket) {
        super(EntityRegistry.getDefinition(HumanEntityDefinition.ID));
        this.server = server;
        this.session = session;

        this.version = session.getVersion();
        this.device = loginPacket.getDevice();
        this.xuid = loginPacket.getXUID();
        this.uuid = loginPacket.getUUID();
        this.username = loginPacket.getUsername();
        this.languageCode = loginPacket.getLanguageCode();
        this.skin = loginPacket.getSkin();
        this.inventory = new ImplPlayerInventory(this);

        this.setDisplayName(this.getUsername());
        this.physicsEngine.setPositionUpdate(false);

        // Players will die at any health lower than 0.5
        this.getAttribute(AttributeType.HEALTH).setMinimumValue(0.5f);
    }

    @Override
    public BaseMinecraftVersion getVersion() {
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
        this.gamemode = gamemode;

        if (this.hasSpawned()) {
            SetPlayerGamemodePacket setPlayerGamemodePacket = new SetPlayerGamemodePacket();
            setPlayerGamemodePacket.setGamemode(gamemode);
            this.sendPacket(setPlayerGamemodePacket);
            this.updateAdventureSettings();
        }
    }

    /**
     * Updates the adventure settings based off of the current gamemode.
     */
    protected void updateAdventureSettings() {
        AdventureSettings adventureSettings = this.getAdventureSettings();
        adventureSettings.setCanFly(this.getGamemode().equals(Gamemode.CREATIVE));
        if (adventureSettings.isFlying()) {
            adventureSettings.setIsFlying(this.getGamemode().equals(Gamemode.CREATIVE));
        }
        this.setAdventureSettings(adventureSettings);
    }

    @Override
    public AdventureSettings getAdventureSettings() {
        return this.adventureSettings.clone();
    }

    @Override
    public void setAdventureSettings(AdventureSettings adventureSettings) {
        ImplAdventureSettings settings = (ImplAdventureSettings) adventureSettings;
        this.adventureSettings = settings;

        AdventureSettingsPacket adventureSettingsPacket = new AdventureSettingsPacket();
        adventureSettingsPacket.setUniqueEntityRuntimeId(this.getId());
        adventureSettingsPacket.setPlayerPermissionLevel(settings.getPlayerPermissionLevel());
        adventureSettingsPacket.setCommandPermissionLevel(settings.getCommandPermissionLevel());
        adventureSettingsPacket.setFlags(settings.getFlags());
        this.sendPacket(adventureSettingsPacket);
    }

    public void onInitialized() {
        this.locallyInitialized = true;

        for (Player player : this.getServer().getPlayers()) {
            if (!this.isHiddenFrom(player) && !player.equals(this)) {
                player.getPlayerList().addEntry(this.getPlayerListEntry());
            }
        }

        this.getChunkManager().onLocallyInitialized();
    }

    public BreakingData getBlockBreakData() {
        return this.breakingData;
    }

    public boolean canReach(Vector3i vector3i, float maxDistance) {
        return this.canReach(vector3i.toVector3(), maxDistance);
    }

    public boolean canReach(Vector3 vector3, float maxDistance) {
        Vector3 position = this.getLocation().add(0, this.getEyeHeight(), 0);

        // Distance check
        double distance = position.distanceBetween(vector3);
        if (distance > maxDistance) {
            return false;
        }

        // Direction check
        Vector3 playerDirectionVector = this.getDirectionVector();
        Vector3 targetDirectionVector = vector3.subtract(this.getLocation().add(0, this.getEyeHeight(), 0)).normalize();
        return playerDirectionVector.dot(targetDirectionVector) > 0;    // Must be in same direction
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

    @Override
    public void setMetaData(EntityMetaData metaData) {
        super.setMetaData(metaData);

        if (this.hasSpawned()) {
            SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
            setEntityDataPacket.setRuntimeId(this.getId());
            setEntityDataPacket.setData(this.getMetaData());
            this.sendPacket(setEntityDataPacket);
        }
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
        if (openInventory.isPresent() && !((BaseInventory) openInventory.get()).closeFor(this)) {
            return false;
        } else {
            this.openInventory = null;
            return true;
        }
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
     * Fetch the SAVED player data from the {@link io.github.willqi.pizzaserver.server.player.playerdata.provider.PlayerDataProvider} if any exists.
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
                    .setLevelName(this.getLevel().getProvider().getFileName())
                    .setDimension(this.getLocation().getWorld().getDimension())
                    .setGamemode(this.getGamemode())
                    .setPosition(this.getLocation())
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
            updateAttributesPacket.setAttributes(attributes);
            this.sendPacket(updateAttributesPacket);
        }
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        this.sendAttribute(this.getAttribute(AttributeType.HEALTH));
        if (NumberUtils.isNearlyEqual(this.getHealth(), this.getMaxHealth())) {
            this.regenerationTicks = 80;
        }
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
    public void teleport(World world, float x, float y, float z) {
        this.teleport(world, x, y, z, world.getDimension());
    }

    @Override
    public void teleport(float x, float y, float z, Dimension transferDimension) {
        this.teleport(this.getWorld(), x, y, z, transferDimension);
    }

    @Override
    public void teleport(Location location, Dimension transferDimension) {
        this.teleport(location.getWorld(), location.getX(), location.getY(), location.getZ(), transferDimension);
    }

    @Override
    public void teleport(World world, float x, float y, float z, Dimension transferDimension) {
        World oldWorld = this.getWorld();

        super.teleport(world, x, y, z);
        MoveEntityAbsolutePacket teleportPacket = new MoveEntityAbsolutePacket();
        teleportPacket.setEntityRuntimeId(this.getId());
        teleportPacket.setPosition(this.getLocation().add(0, this.getEyeHeight(), 0));
        teleportPacket.setPitch(this.getPitch());
        teleportPacket.setYaw(this.getYaw());
        teleportPacket.setHeadYaw(this.getHeadYaw());
        teleportPacket.addFlag(MoveEntityAbsolutePacket.Flag.TELEPORT);
        this.sendPacket(teleportPacket);

        if (!oldWorld.getDimension().equals(transferDimension)) {
            this.setDimensionTransferScreen(transferDimension);
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
            changeDimensionPacket.setDimension(dimension);
            changeDimensionPacket.setPosition(this.getLocation().add(0, this.getEyeHeight(), 0));
            if (!this.isAlive()) {
                changeDimensionPacket.setRespawnResponse(true);
            }
            this.sendPacket(changeDimensionPacket);
            this.getChunkManager().onDimensionTransfer();
        }
    }

    @Override
    public void sendMessage(TextMessage message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setType(message.getType());
        textPacket.setMessage(message.getMessage());
        textPacket.setRequiresTranslation(message.requiresTranslation());
        textPacket.setSourceName(message.getSourceName());
        textPacket.setParameters(message.getParameters());
        textPacket.setXuid(message.getXuid());
        textPacket.setPlatformChatId(message.getPlatformChatId());
        this.sendPacket(textPacket);
    }

    @Override
    public void sendMessage(String message) {
        this.sendMessage(new TextMessage.Builder()
                .setType(TextType.RAW)
                .setMessage(message)
                .build());
    }

    @Override
    public void sendPlayerMessage(Player sender, String message) {
        this.sendMessage(new TextMessage.Builder()
                        .setType(TextType.CHAT)
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
    public boolean isConnected() {
        return !this.session.isDisconnected();
    }

    @Override
    public long getPing() {
        return this.session.getPing();
    }

    @Override
    public void sendPacket(BaseBedrockPacket packet) {
        this.session.sendPacket(packet);
    }

    @Override
    public void disconnect() {
        this.session.disconnect();
    }

    @Override
    public void disconnect(String reason) {
        DisconnectPacket disconnectPacket = new DisconnectPacket();
        disconnectPacket.setKickMessage(reason);
        this.sendPacket(disconnectPacket);
        this.session.disconnect();
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
        // Make sure that the block we're breaking is within reach!
        boolean stopBreakingBlock = this.getBlockBreakData().getBlock().isPresent()
                && !(this.canReach(this.getBlockBreakData().getBlock().get().getLocation(), this.getGamemode().equals(Gamemode.CREATIVE) ? 13 : 7)
                        && this.isAlive()
                        && this.getAdventureSettings().canMine());
        if (stopBreakingBlock) {
            BlockStopBreakEvent blockStopBreakEvent = new BlockStopBreakEvent(this, this.getBlockBreakData().getBlock().get());
            this.getServer().getEventManager().call(blockStopBreakEvent);

            this.getBlockBreakData().stopBreaking();
        }

        if (!NumberUtils.isNearlyEqual(this.getHealth(), this.getMaxHealth()) && this.getFoodLevel() >= 18) {
            if (this.regenerationTicks == 0) {
                this.regenerationTicks = 80;
                this.setHealth(this.getHealth() + 1);
            }
            this.regenerationTicks--;
        }

        super.tick();
    }

    @Override
    public void moveTo(float x, float y, float z) {
        Location oldLocation = new Location(this.world, new Vector3(this.x, this.y, this.z));
        super.moveTo(x, y, z);

        if (!oldLocation.getChunk().equals(this.getChunk())) {
            this.getChunkManager().onEnterNewChunk(oldLocation);
        }
    }

    @Override
    public void onSpawned() {
        super.onSpawned();
        this.getChunkManager().onSpawned();
        this.completeLogin();
    }

    @Override
    public void onDespawned() {
        super.onDespawned();
        this.getChunkManager().onDespawn();
    }

    private void completeLogin() {
        this.getInventory().sendSlots(this);
        this.getMetaData().setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.HAS_GRAVITY, true);
        this.getMetaData().setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.IS_BREATHING, true);
        this.getMetaData().setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.CAN_WALL_CLIMB, true);
        this.setMetaData(this.getMetaData());
        this.sendAttributes();

        this.updateAdventureSettings();

        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.setStatus(PlayStatusPacket.PlayStatus.PLAYER_SPAWN);
        this.sendPacket(playStatusPacket);
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
