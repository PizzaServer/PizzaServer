package io.github.willqi.pizzaserver.server.player;

import io.github.willqi.pizzaserver.api.entity.inventory.Inventory;
import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.event.type.player.PlayerStartSneakingEvent;
import io.github.willqi.pizzaserver.api.event.type.player.PlayerStopSneakingEvent;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.player.PlayerList;
import io.github.willqi.pizzaserver.api.player.attributes.Attribute;
import io.github.willqi.pizzaserver.api.player.attributes.PlayerAttributes;
import io.github.willqi.pizzaserver.api.player.skin.Skin;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Tuple;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.entity.BaseLivingEntity;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlagCategory;
import io.github.willqi.pizzaserver.server.entity.inventory.BaseInventory;
import io.github.willqi.pizzaserver.server.entity.inventory.ImplPlayerInventory;
import io.github.willqi.pizzaserver.server.network.BedrockClientSession;
import io.github.willqi.pizzaserver.server.network.protocol.data.MovementMode;
import io.github.willqi.pizzaserver.server.network.protocol.data.WorldEventType;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.api.player.attributes.AttributeType;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.api.player.data.Device;
import io.github.willqi.pizzaserver.server.player.playerdata.PlayerData;
import io.github.willqi.pizzaserver.server.level.world.chunks.ImplChunk;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;

public class ImplPlayer extends BaseLivingEntity implements Player {

    private final ImplServer server;
    private final BedrockClientSession session;
    private boolean autoSave = true;

    private final BaseMinecraftVersion version;
    private final Device device;
    private final String xuid;
    private final UUID uuid;
    private final String username;
    private final String languageCode;

    private final PlayerList playerList = new ImplPlayerList(this);
    private Skin skin;

    private int chunkRadius = 3;
    private final AtomicInteger chunkRequestsLeft = new AtomicInteger();    // Reset every tick

    private final PlayerAttributes attributes = new PlayerAttributes();

    private Inventory openInventory = null;

    private Vector3i blockBreakingCoordinates;
    private int blockBreakingTicksLeft;


    public ImplPlayer(ImplServer server, BedrockClientSession session, LoginPacket loginPacket) {
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

        this.chunkRequestsLeft.set(server.getConfig().getChunkRequestsPerTick());
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
    public Skin getSkin() {
        return this.skin;
    }

    @Override
    public void setSkin(Skin newSkin) {
        this.skin = newSkin;
        PlayerSkinPacket playerSkinPacket = new PlayerSkinPacket();
        playerSkinPacket.setPlayerUUID(this.getUUID());
        playerSkinPacket.setSkin(newSkin);
        playerSkinPacket.setTrusted(newSkin.isTrusted());

        for (Player viewer : this.getViewers()) {
            viewer.sendPacket(playerSkinPacket);
        }
        this.sendPacket(playerSkinPacket);
    }

    @Override
    public boolean isSneaking() {
        return this.getMetaData().hasFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.IS_SNEAKING);
    }

    @Override
    public void setSneaking(boolean sneaking) {
        boolean isCurrentlySneaking = this.isSneaking();
        boolean updateSneakingData = false;
        if (sneaking && !isCurrentlySneaking) {
            PlayerStartSneakingEvent event = new PlayerStartSneakingEvent(this);
            this.getServer().getEventManager().call(event);
            updateSneakingData = true;
        } else if (!sneaking && isCurrentlySneaking) {
            PlayerStopSneakingEvent event = new PlayerStopSneakingEvent(this);
            this.getServer().getEventManager().call(event);
            updateSneakingData = true;
        }

        if (updateSneakingData) {
            this.getMetaData().setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.IS_SNEAKING, sneaking);
            this.setMetaData(this.getMetaData());
        }
    }

    public void setBlockBreaking(Vector3i blockCoordinates) {
        if (this.blockBreakingCoordinates != null && !this.blockBreakingCoordinates.equals(blockCoordinates)) {
            // Stop breaking the previous block
            Block previouslyMiningBlock = this.getWorld().getBlock(this.blockBreakingCoordinates);
            if (!previouslyMiningBlock.isAir()) { // In case someone breaks the block we're breaking
                this.sendMessage("stopped breaking previous block");
                WorldEventPacket breakStopPacket = new WorldEventPacket();
                breakStopPacket.setType(WorldEventType.EVENT_BLOCK_STOP_BREAK);
                breakStopPacket.setPosition(this.blockBreakingCoordinates.toVector3());
                for (Player player : this.getChunk().getViewers()) {
                    player.sendPacket(breakStopPacket);
                }
            }
        }

        this.blockBreakingCoordinates = blockCoordinates;
        this.blockBreakingTicksLeft = 0;

        if (this.blockBreakingCoordinates != null) {
            // TODO: calculate amount of ticks it takes to break this block
            this.blockBreakingTicksLeft = 10;
            this.sendMessage("started to break new block");

            WorldEventPacket breakStartPacket = new WorldEventPacket();
            breakStartPacket.setType(WorldEventType.EVENT_BLOCK_STOP_BREAK);
            breakStartPacket.setPosition(this.blockBreakingCoordinates.toVector3());
            breakStartPacket.setData(20);
            for (Player player : this.getChunk().getViewers()) {
                player.sendPacket(breakStartPacket);
            }
        }
    }

    @Override
    public Optional<Block> getBlockBreaking() {
        if (this.blockBreakingCoordinates != null) {
            return Optional.of(this.world.getBlock(this.blockBreakingCoordinates));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public float getHeight() {
        return 1.8f;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getEyeHeight() {
        return 1.62f;
    }

    public boolean canReach(Vector3i vector3i) {
        return this.canReach(vector3i.toVector3());
    }

    public boolean canReach(Vector3 vector3) {
        Vector3 position = this.getLocation().add(0, this.getEyeHeight(), 0);

        // Distance check
        // TODO: take into account creative mode when gamemodes are implemented
        double distance = position.distanceBetween(vector3);
        if (distance > 7) {
            return false;
        }

        // Direction check
        Vector3 playerDirectionVector = this.getDirectionVector();
        Vector3 targetDirectionVector = vector3.subtract(this.getLocation()).normalize();
        return playerDirectionVector.dot(targetDirectionVector) > 0;    // Must be in same direction
    }

    @Override
    public void setMetaData(EntityMetaData metaData) {
        super.setMetaData(metaData);

        SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
        setEntityDataPacket.setRuntimeId(this.getId());
        setEntityDataPacket.setData(this.getMetaData());
        for (Player player : this.getViewers()) {
            player.sendPacket(setEntityDataPacket);
        }
        this.sendPacket(setEntityDataPacket);
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

            Location location = this.getLocation();

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

            // Remove player from chunks they can observe
            for (int x = -this.getChunkRadius(); x <= this.getChunkRadius(); x++) {
                for (int z = -this.getChunkRadius(); z <= this.getChunkRadius(); z++) {
                    // Chunk radius is circular
                    int distance = (int) Math.round(Math.sqrt((x * x) + (z * z)));
                    if (this.chunkRadius > distance) {
                        ImplChunk chunk = (ImplChunk) location.getWorld().getChunkManager().getChunk(location.getChunkX() + x, location.getChunkZ() + z);
                        chunk.despawnFrom(this);
                    }
                }
            }
        }
    }

    @Override
    public PlayerAttributes getAttributes() {
        return this.attributes;
    }

    public void sendAttributes() {
        this.sendAttributes(this.attributes.getAttributes());
    }

    private void sendAttributes(Set<Attribute> attributes) {
        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
        updateAttributesPacket.setRuntimeEntityId(this.getId());
        updateAttributesPacket.setAttributes(attributes);
        this.sendPacket(updateAttributesPacket);
    }

    private void sendAttribute(Attribute attribute) {
        this.sendAttributes(Collections.singleton(attribute));
    }

    @Override
    public float getHealth() {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.HEALTH);
        return attribute.getCurrentValue();
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.HEALTH);
        attribute.setCurrentValue(health);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getMaxHealth() {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.HEALTH);
        return attribute.getMaximumValue();
    }

    @Override
    public void setMaxHealth(float maxHealth) {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.HEALTH);
        attribute.setMaximumValue(maxHealth);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getAbsorption() {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.ABSORPTION);
        return attribute.getCurrentValue();
    }

    @Override
    public void setAbsorption(float absorption) {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.ABSORPTION);
        attribute.setCurrentValue(absorption);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getMaxAbsorption() {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.ABSORPTION);
        return attribute.getMaximumValue();
    }

    @Override
    public void setMaxAbsorption(float maxAbsorption) {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.ABSORPTION);
        attribute.setMaximumValue(maxAbsorption);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getMovementSpeed() {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.MOVEMENT_SPEED);
        return attribute.getCurrentValue();
    }

    @Override
    public void setMovementSpeed(float movementSpeed) {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.MOVEMENT_SPEED);
        attribute.setCurrentValue(movementSpeed);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getFoodLevel() {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.FOOD);
        return attribute.getCurrentValue();
    }

    @Override
    public void setFoodLevel(float foodLevel) {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.FOOD);
        attribute.setCurrentValue(foodLevel);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getSaturationLevel() {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.SATURATION);
        return attribute.getCurrentValue();
    }

    @Override
    public void setSaturationLevel(float saturationLevel) {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.SATURATION);
        attribute.setCurrentValue(saturationLevel);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getExperience() {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.EXPERIENCE);
        return attribute.getCurrentValue();
    }

    @Override
    public void setExperience(float experience) {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.EXPERIENCE);
        attribute.setCurrentValue(experience);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public int getExperienceLevel() {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.EXPERIENCE_LEVEL);
        return (int) attribute.getCurrentValue();
    }

    @Override
    public void setExperienceLevel(int experienceLevel) {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.EXPERIENCE_LEVEL);
        attribute.setCurrentValue(experienceLevel);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public void sendMessage(String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setType(TextPacket.TextType.RAW);
        textPacket.setMessage(message);
        this.sendPacket(textPacket);
    }

    @Override
    public void sendPlayerMessage(Player sender, String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setType(TextPacket.TextType.CHAT);
        textPacket.setSourceName(sender.getUsername());
        textPacket.setMessage(message);
        textPacket.setXuid(sender.getXUID());
        this.sendPacket(textPacket);
    }

    @Override
    public int getChunkRadius() {
        return Math.min(this.chunkRadius, this.server.getConfig().getChunkRadius());
    }

    @Override
    public void setChunkRadiusRequested(int radius) {
        int oldRadius = this.getChunkRadius();
        this.chunkRadius = Math.min(radius, this.getServer().getConfig().getChunkRadius());

        if (this.hasSpawned()) {
            this.updateVisibleChunks(this.getLocation(), oldRadius);
        }
    }

    @Override
    public void requestSendChunk(int x, int z) {
        this.getLocation().getWorld().getChunkManager().sendPlayerChunk(this, x, z, true);
    }

    /**
     * Check if a player can be sent a chunk this tick.
     * Requests are reset during an entity's tick
     * @return whether or not the player should be sent a chunk this tick
     */
    public boolean acknowledgeChunkSendRequest() {
        return this.chunkRequestsLeft.getAndDecrement() > 0;
    }

    private void sendNetworkChunkPublisher() {
        NetworkChunkPublisherUpdatePacket packet = new NetworkChunkPublisherUpdatePacket();
        packet.setCoordinates(this.getLocation().toVector3i());
        packet.setRadius(this.getChunkRadius() * 16);
        this.sendPacket(packet);
    }

    /**
     * Sends and removes chunks the player can and cannot see.
     */
    private void updateVisibleChunks(Location oldLocation, int oldChunkRadius) {
        Set<Tuple<Integer, Integer>> chunksToRemove = new HashSet<>();
        if (oldLocation != null) {
            // What were our previous chunks loaded?
            int oldPlayerChunkX = oldLocation.getChunkX();
            int oldPlayerChunkZ = oldLocation.getChunkZ();
            for (int x = -oldChunkRadius; x <= oldChunkRadius; x++) {
                for (int z = -oldChunkRadius; z <= oldChunkRadius; z++) {
                    // Chunk radius is circular
                    int distance = (int) Math.round(Math.sqrt((x * x) + (z * z)));
                    if (oldChunkRadius > distance) {
                        chunksToRemove.add(new Tuple<>(oldPlayerChunkX + x, oldPlayerChunkZ + z));
                    }
                }
            }
        }

        // What are our new chunks loaded?
        int currentPlayerChunkX = this.getLocation().getChunkX();
        int currentPlayerChunkZ = this.getLocation().getChunkZ();
        for (int x = -this.getChunkRadius(); x <= this.getChunkRadius(); x++) {
            for (int z = -this.getChunkRadius(); z <= this.getChunkRadius(); z++) {
                // Chunk radius is circular
                int distance = (int) Math.round(Math.sqrt((x * x) + (z * z)));
                if (this.getChunkRadius() > distance) {
                    // Ensure that this chunk is not already visible
                    if (!chunksToRemove.remove(new Tuple<>((currentPlayerChunkX + x), (currentPlayerChunkZ + z)))) {
                        this.requestSendChunk(currentPlayerChunkX + x, currentPlayerChunkZ + z);
                    }
                }
            }
        }

        // Remove each chunk we shouldn't get packets from
        for (Tuple<Integer, Integer> key : chunksToRemove) {
            ((ImplChunk) this.getLocation().getWorld().getChunkManager().getChunk(key.getObjectA(), key.getObjectB())).despawnFrom(this);
        }

        this.sendNetworkChunkPublisher();
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
        this.chunkRequestsLeft.set(this.getServer().getConfig().getChunkRequestsPerTick()); // Reset amount of chunks that we can be sent this tick

        if (this.moveUpdate) {
            MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
            movePlayerPacket.setEntityRuntimeId(this.getId());
            movePlayerPacket.setPosition(new Vector3(this.getX(), this.getY() + this.getEyeHeight(), this.getZ()));
            movePlayerPacket.setPitch(this.getPitch());
            movePlayerPacket.setYaw(this.getYaw());
            movePlayerPacket.setHeadYaw(this.getHeadYaw());
            movePlayerPacket.setMode(MovementMode.NORMAL);
            movePlayerPacket.setOnGround(false);

            for (Player player : this.getViewers()) {
                player.sendPacket(movePlayerPacket);
            }
        }

        Optional<Block> miningBlock = this.getBlockBreaking();
        if (miningBlock.isPresent()) {
            if (this.canReach(miningBlock.get().getLocation())) {
                this.sendMessage("can reach and ticks = " + this.blockBreakingTicksLeft);
                if (this.blockBreakingTicksLeft-- <= 0) {
                    this.getWorld().setBlock(miningBlock.get().getBlockType().getResultantBlock(), this.blockBreakingCoordinates);
                    this.setBlockBreaking(null);
                    this.sendMessage("mined!");
                }
            } else {
                // The block we're trying to mine is too far from us.
                this.setBlockBreaking(null);
                this.sendMessage("can on longer reach");
            }
        }

        super.tick();
    }

    @Override
    public void moveTo(float x, float y, float z) {
        Location oldLocation = new Location(this.world, new Vector3(this.x, this.y, this.z));
        super.moveTo(x, y, z);

        if (!oldLocation.getChunk().equals(this.getChunk())) {
            this.updateVisibleChunks(oldLocation, this.getChunkRadius());
        }
    }

    @Override
    public void onSpawned() {
        super.onSpawned();
        this.sendNetworkChunkPublisher();   // Load chunks sent during initial login handshake

        this.updateVisibleChunks(null, this.chunkRadius);
        this.completeLogin();
    }

    private void completeLogin() {
        this.getInventory().sendSlots(this);
        this.getMetaData().setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.HAS_GRAVITY, true);
        this.getMetaData().setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.IS_BREATHING, true);
        this.getMetaData().setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.CAN_WALL_CLIMB, true);
        this.setMetaData(this.getMetaData());
        this.sendAttributes();

        // Update every other player's player list to include this player
        for (Player player : this.getServer().getPlayers()) {
            if (!this.isHiddenFrom(player) && !player.equals(this)) {
                player.getPlayerList().addEntry(this.getPlayerListEntry());
            }
        }

        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.setStatus(PlayStatusPacket.PlayStatus.PLAYER_SPAWN);
        this.sendPacket(playStatusPacket);

    }

    @Override
    public PlayerList.Entry getPlayerListEntry() {
        return new PlayerList.Entry.Builder()
                .setUUID(this.getUUID())
                .setXUID(this.getXUID())
                .setUsername(this.getUsername())
                .setEntityRuntimeId(this.getId())
                .setDevice(this.getDevice())
                .setSkin(this.getSkin())
                .build();
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

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);

        AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
        addPlayerPacket.setUUID(this.getUUID());
        addPlayerPacket.setUsername(this.getUsername());
        addPlayerPacket.setEntityRuntimeId(this.getId());
        addPlayerPacket.setEntityUniqueId(this.getId());
        addPlayerPacket.setPosition(new Vector3(this.getX(), this.getY(), this.getZ()));
        addPlayerPacket.setVelocity(new Vector3(0, 0, 0));
        addPlayerPacket.setPitch(this.getPitch());
        addPlayerPacket.setYaw(this.getYaw());
        addPlayerPacket.setHeadYaw(this.getHeadYaw());
        addPlayerPacket.setMetaData(this.getMetaData());
        addPlayerPacket.setDevice(this.getDevice());
        addPlayerPacket.setHeldItem(this.getInventory().getHeldItem());
        player.sendPacket(addPlayerPacket);
    }

}
