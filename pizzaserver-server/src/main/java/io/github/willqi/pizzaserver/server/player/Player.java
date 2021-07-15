package io.github.willqi.pizzaserver.server.player;

import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.entity.LivingEntity;
import io.github.willqi.pizzaserver.server.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.server.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.server.entity.meta.flags.EntityMetaFlagType;
import io.github.willqi.pizzaserver.server.network.BedrockClientSession;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.server.player.attributes.Attribute;
import io.github.willqi.pizzaserver.server.player.attributes.AttributeType;
import io.github.willqi.pizzaserver.server.player.attributes.PlayerAttributes;
import io.github.willqi.pizzaserver.server.player.data.Device;
import io.github.willqi.pizzaserver.server.player.skin.Skin;
import io.github.willqi.pizzaserver.server.utils.Location;
import io.github.willqi.pizzaserver.server.world.chunks.Chunk;
import io.github.willqi.pizzaserver.server.world.chunks.ChunkManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Player extends LivingEntity {

    private final Server server;
    private final BedrockClientSession session;

    private final MinecraftVersion version;
    private final Device device;
    private final String xuid;
    private final UUID uuid;
    private final String username;
    private final String languageCode;
    private Skin skin;

    private int chunkRadius;

    private final PlayerAttributes attributes = new PlayerAttributes();


    public Player(Server server, BedrockClientSession session, LoginPacket loginPacket) {
        this.server = server;
        this.session = session;

        this.version = session.getVersion();
        this.device = loginPacket.getDevice();
        this.xuid = loginPacket.getXuid();
        this.uuid = loginPacket.getUuid();
        this.username = loginPacket.getUsername();
        this.languageCode = loginPacket.getLanguageCode();
        this.skin = loginPacket.getSkin();

        this.chunkRadius = server.getConfig().getChunkRadius();
    }

    public MinecraftVersion getVersion() {
        return this.version;
    }

    public Device getDevice() {
        return this.device;
    }

    public String getXuid() {
        return this.xuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public Skin getSkin() {
        return this.skin;
    }

    public void setSkin(Skin newSkin) {
        // TODO: packet level stuff for player skin updates
        this.skin = newSkin;
    }

    @Override
    public void setMetaData(EntityMetaData metaData) {
        super.setMetaData(metaData);

        SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
        setEntityDataPacket.setRuntimeId(this.getId());
        setEntityDataPacket.setData(this.getMetaData());
        this.sendPacket(setEntityDataPacket);
    }

    public int getChunkRadius() {
        return Math.min(this.chunkRadius, this.server.getConfig().getChunkRadius());
    }

    public void setChunkRadiusRequested(int radius) {
        int oldRadius = this.chunkRadius;
        this.chunkRadius = radius;
        if (this.hasSpawned()) {
            this.updateVisibleChunks(this.getLocation(), oldRadius);
        }
    }

    public boolean canSeeChunk(Chunk chunk) {
        return (this.getChunkRadius() + this.getLocation().getChunkX() >= chunk.getX()) && (this.getLocation().getChunkX() - this.getChunkRadius() <= chunk.getX()) &&
                (this.getChunkRadius() + this.getLocation().getChunkZ() >= chunk.getZ()) && (this.getLocation().getChunkZ() - this.getChunkRadius() <= chunk.getZ());
    }

    public Server getServer() {
        return this.server;
    }

    public void sendPacket(BedrockPacket packet) {
        this.session.queueSendPacket(packet);
    }

    public void disconnect() {
        this.session.disconnect();
    }

    public void disconnect(String reason) {
        DisconnectPacket disconnectPacket = new DisconnectPacket();
        disconnectPacket.setKickMessage(reason);
        this.sendPacket(disconnectPacket);
        this.session.disconnect();
    }

    /**
     * Called when the server registers that the player is disconnected.
     * It cleans up data for this player
     */
    public void onDisconnect() {
        if (this.hasSpawned()) {
            this.getLocation().getWorld().removeEntity(this);

            // Remove player from chunks they can observe
            for (int chunkX = this.getLocation().getChunkX() - this.getChunkRadius(); chunkX <= this.getLocation().getChunkX() + this.getChunkRadius(); chunkX++) {
                for (int chunkZ = this.getLocation().getChunkZ() - this.getChunkRadius(); chunkZ <= this.getLocation().getChunkZ() + this.getChunkRadius(); chunkZ++) {
                    Chunk chunk = this.getLocation().getWorld().getChunkManager().getChunk(chunkX, chunkZ);
                    chunk.despawnFrom(this);
                }
            }
        }
    }

    public PlayerAttributes getAttributes() {
        return this.attributes;
    }

    public void sendAttributes() {
        this.sendAttributes(this.attributes.getAttributes());
    }

    private void sendAttribute(Attribute attribute) {
        this.sendAttributes(Collections.singleton(attribute));
    }

    private void sendAttributes(Set<Attribute> attributes) {
        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
        updateAttributesPacket.setRuntimeEntityId(this.getId());
        updateAttributesPacket.setAttributes(attributes);
        this.sendPacket(updateAttributesPacket);
    }

    @Override
    public float getHealth() {
        return super.getHealth();
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

    public float getFoodLevel() {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.FOOD);
        return attribute.getCurrentValue();
    }

    public void setFoodLevel(float foodLevel) {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.FOOD);
        attribute.setCurrentValue(foodLevel);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    public float getSaturationLevel() {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.SATURATION);
        return attribute.getCurrentValue();
    }

    public void setSaturationLevel(float saturationLevel) {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.SATURATION);
        attribute.setCurrentValue(saturationLevel);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    public float getExperience() {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.EXPERIENCE);
        return attribute.getCurrentValue();
    }

    public void setExperience(float experience) {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.EXPERIENCE);
        attribute.setCurrentValue(experience);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    public float getExperienceLevel() {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.EXPERIENCE_LEVEL);
        return attribute.getCurrentValue();
    }

    public void setExperienceLevel(int experienceLevel) {
        Attribute attribute = this.getAttributes().getAttribute(AttributeType.EXPERIENCE_LEVEL);
        attribute.setCurrentValue(experienceLevel);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public void setLocation(Location newLocation) {
        Location oldLocation = this.getLocation();
        super.setLocation(newLocation);

        if (this.hasSpawned()) {    // Do we need to send new chunks?
            boolean shouldUpdateChunks = (oldLocation == null) || (oldLocation.getChunkX() != newLocation.getChunkX()) ||
                                            (oldLocation.getChunkZ() != newLocation.getChunkZ()) ||
                                            !(oldLocation.getWorld().equals(this.getLocation().getWorld()));
            if (shouldUpdateChunks) {
                this.updateVisibleChunks(oldLocation, this.chunkRadius);
            }
        }
    }

    @Override
    public void onSpawned() {
        this.updateVisibleChunks(null, this.chunkRadius);

        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.setStatus(PlayStatusPacket.PlayStatus.PLAYER_SPAWN);
        this.sendPacket(playStatusPacket);

        this.getMetaData().setFlag(EntityMetaFlagType.DATA_FLAG, EntityMetaFlag.HAS_GRAVITY, true);
        this.getMetaData().setFlag(EntityMetaFlagType.DATA_FLAG, EntityMetaFlag.IS_BREATHING, true);
        this.setMetaData(this.getMetaData());
        this.sendAttributes();
    }

    @Override
    public void spawnTo(Player player) {
        // TODO: implement in order for multiplayer to work properly
    }

    /**
     * Request a chunk in the player's world to be sent to the player.
     * This does not send it immediately, but rather requests the server to send the chunk.
     * @param x
     * @param z
     */
    public void sendChunk(int x, int z) {
        ChunkManager chunkManager = this.getLocation().getWorld().getChunkManager();
        if (chunkManager.isChunkLoaded(x, z)) {
            chunkManager.addChunkToPlayerQueue(this, chunkManager.getChunk(x, z));
        } else {
            chunkManager.fetchChunk(x, z).whenComplete((chunk, exception) -> {
                if (exception != null) {
                    Server.getInstance().getLogger().error("Failed to send chunk (" + x + ", " + z + ") to player " + this.getUsername(), exception);
                    return;
                }
                chunkManager.addChunkToPlayerQueue(this, chunk);
            });
        }
    }

    private void sendNetworkChunkPublisher() {
        NetworkChunkPublisherUpdatePacket packet = new NetworkChunkPublisherUpdatePacket();
        packet.setCoordinates(this.getLocation().toVector3i());
        packet.setRadius(this.getChunkRadius() * 16);
        this.sendPacket(packet);
    }

    /**
     * Sends and removes chunks the player can and cannot see
     */
    private void updateVisibleChunks(Location oldLocation, int oldChunkRadius) {
        Set<Chunk> chunksToRemove = new HashSet<>();

        if (oldLocation != null) {
            // What were our previous chunks loaded?
            for (int chunkX = oldLocation.getChunkX() - oldChunkRadius; chunkX <= oldLocation.getChunkX() + oldChunkRadius; chunkX++) {
                for (int chunkZ = oldLocation.getChunkZ() - oldChunkRadius; chunkZ <= oldLocation.getChunkZ() + oldChunkRadius; chunkZ++) {
                    if (oldLocation.getWorld().getChunkManager().isChunkLoaded(chunkX, chunkZ)) {
                        Chunk chunk = oldLocation.getWorld().getChunkManager().getChunk(chunkX, chunkZ);
                        chunksToRemove.add(chunk);
                    }
                }
            }
        }

        // What are our new chunks loaded?
        boolean requiresChunkPublisher = false;
        for (int chunkX = this.getLocation().getChunkX() - this.getChunkRadius(); chunkX <= this.getLocation().getChunkX() + this.getChunkRadius(); chunkX++) {
            for (int chunkZ = this.getLocation().getChunkZ() - this.getChunkRadius(); chunkZ <= this.getLocation().getChunkZ() + this.getChunkRadius(); chunkZ++) {
                if (this.getLocation().getWorld().getChunkManager().isChunkLoaded(chunkX, chunkZ)) {
                    Chunk chunk = this.getLocation().getWorld().getChunkManager().getChunk(chunkX, chunkZ);
                    if (chunksToRemove.remove(chunk)) {
                        continue;   // We don't need to send this chunk
                    }
                }
                requiresChunkPublisher = true;
                this.sendChunk(chunkX, chunkZ);
            }
        }

        // Remove each chunk we shouldn't get packets from
        for (Chunk chunk : chunksToRemove) {
            chunk.despawnFrom(this);
        }

        if (requiresChunkPublisher) {
            this.sendNetworkChunkPublisher();
        }
    }

    /**
     * Send a raw message
     * @param message
     */
    public void sendMessage(String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setType(TextPacket.TextType.RAW);
        textPacket.setMessage(message);
        this.sendPacket(textPacket);
    }

    /**
     * Send a message originating from a player
     * @param sender
     * @param message
     */
    public void sendPlayerMessage(Player sender, String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setType(TextPacket.TextType.CHAT);
        textPacket.setSourceName(sender.getUsername());
        textPacket.setMessage(message);
        textPacket.setXuid(sender.getXuid());
        this.sendPacket(textPacket);
    }


}
