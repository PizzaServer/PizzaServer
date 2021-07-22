package io.github.willqi.pizzaserver.server.player;

import io.github.willqi.pizzaserver.api.network.protocol.packets.APIBedrockPacket;
import io.github.willqi.pizzaserver.api.network.protocol.versions.APIMinecraftVersion;
import io.github.willqi.pizzaserver.api.player.APIPlayer;
import io.github.willqi.pizzaserver.api.player.attributes.APIAttribute;
import io.github.willqi.pizzaserver.api.player.attributes.APIPlayerAttributes;
import io.github.willqi.pizzaserver.api.player.skin.APISkin;
import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.entity.LivingEntity;
import io.github.willqi.pizzaserver.server.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.server.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.server.entity.meta.flags.EntityMetaFlagType;
import io.github.willqi.pizzaserver.server.network.BedrockClientSession;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.api.player.attributes.AttributeType;
import io.github.willqi.pizzaserver.server.player.attributes.PlayerAttributes;
import io.github.willqi.pizzaserver.api.player.data.Device;
import io.github.willqi.pizzaserver.server.utils.Location;
import io.github.willqi.pizzaserver.server.world.chunks.Chunk;
import io.github.willqi.pizzaserver.server.world.chunks.ChunkManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Player extends LivingEntity implements APIPlayer {

    private final Server server;
    private final BedrockClientSession session;

    private final MinecraftVersion version;
    private final Device device;
    private final String xuid;
    private final UUID uuid;
    private final String username;
    private final String languageCode;
    private APISkin skin;

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

    @Override
    public APIMinecraftVersion getVersion() {
        return this.version;
    }

    @Override
    public Device getDevice() {
        return this.device;
    }

    @Override
    public String getXuid() {
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
    public APISkin getSkin() {
        return this.skin;
    }

    @Override
    public void setSkin(APISkin newSkin) {
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

    @Override
    public int getChunkRadius() {
        return Math.min(this.chunkRadius, this.server.getConfig().getChunkRadius());
    }

    @Override
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

    @Override
    public void sendPacket(APIBedrockPacket packet) {
        this.session.queueSendPacket(packet);
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

    @Override
    public APIPlayerAttributes getAttributes() {
        return this.attributes;
    }

    public void sendAttributes() {
        this.sendAttributes(this.attributes.getAttributes());
    }

    private void sendAttribute(APIAttribute attribute) {
        this.sendAttributes(Collections.singleton(attribute));
    }

    private void sendAttributes(Set<APIAttribute> attributes) {
        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
        updateAttributesPacket.setRuntimeEntityId(this.getId());
        updateAttributesPacket.setAttributes(attributes);
        this.sendPacket(updateAttributesPacket);
    }

    @Override
    public float getHealth() {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.HEALTH);
        return attribute.getCurrentValue();
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.HEALTH);
        attribute.setCurrentValue(health);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getMaxHealth() {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.HEALTH);
        return attribute.getMaximumValue();
    }

    @Override
    public void setMaxHealth(float maxHealth) {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.HEALTH);
        attribute.setMaximumValue(maxHealth);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getAbsorption() {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.ABSORPTION);
        return attribute.getCurrentValue();
    }

    @Override
    public void setAbsorption(float absorption) {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.ABSORPTION);
        attribute.setCurrentValue(absorption);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getMaxAbsorption() {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.ABSORPTION);
        return attribute.getMaximumValue();
    }

    @Override
    public void setMaxAbsorption(float maxAbsorption) {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.ABSORPTION);
        attribute.setMaximumValue(maxAbsorption);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getMovementSpeed() {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.MOVEMENT_SPEED);
        return attribute.getCurrentValue();
    }

    @Override
    public void setMovementSpeed(float movementSpeed) {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.MOVEMENT_SPEED);
        attribute.setCurrentValue(movementSpeed);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getFoodLevel() {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.FOOD);
        return attribute.getCurrentValue();
    }

    @Override
    public void setFoodLevel(float foodLevel) {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.FOOD);
        attribute.setCurrentValue(foodLevel);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getSaturationLevel() {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.SATURATION);
        return attribute.getCurrentValue();
    }

    @Override
    public void setSaturationLevel(float saturationLevel) {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.SATURATION);
        attribute.setCurrentValue(saturationLevel);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public float getExperience() {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.EXPERIENCE);
        return attribute.getCurrentValue();
    }

    @Override
    public void setExperience(float experience) {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.EXPERIENCE);
        attribute.setCurrentValue(experience);
        this.getAttributes().setAttribute(attribute);
        this.sendAttribute(attribute);
    }

    @Override
    public int getExperienceLevel() {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.EXPERIENCE_LEVEL);
        return (int)attribute.getCurrentValue();
    }

    @Override
    public void setExperienceLevel(int experienceLevel) {
        APIAttribute attribute = this.getAttributes().getAttribute(AttributeType.EXPERIENCE_LEVEL);
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

    @Override
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

    @Override
    public void sendMessage(String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setType(TextPacket.TextType.RAW);
        textPacket.setMessage(message);
        this.sendPacket(textPacket);
    }

    @Override
    public void sendPlayerMessage(APIPlayer sender, String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setType(TextPacket.TextType.CHAT);
        textPacket.setSourceName(sender.getUsername());
        textPacket.setMessage(message);
        textPacket.setXuid(sender.getXuid());
        this.sendPacket(textPacket);
    }


}
