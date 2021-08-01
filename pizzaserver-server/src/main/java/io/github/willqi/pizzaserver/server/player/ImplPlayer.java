package io.github.willqi.pizzaserver.server.player;

import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BedrockPacket;
import io.github.willqi.pizzaserver.api.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.player.attributes.Attribute;
import io.github.willqi.pizzaserver.api.player.attributes.PlayerAttributes;
import io.github.willqi.pizzaserver.api.player.skin.Skin;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.commons.utils.Tuple;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.entity.BaseLivingEntity;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlagCategory;
import io.github.willqi.pizzaserver.server.network.BedrockClientSession;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.api.player.attributes.AttributeType;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.server.player.attributes.ImplPlayerAttributes;
import io.github.willqi.pizzaserver.api.player.data.Device;
import io.github.willqi.pizzaserver.server.utils.ImplLocation;
import io.github.willqi.pizzaserver.server.world.chunks.ImplChunk;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ImplPlayer extends BaseLivingEntity implements Player {

    private final ImplServer server;
    private final BedrockClientSession session;

    private final BaseMinecraftVersion version;
    private final Device device;
    private final String xuid;
    private final UUID uuid;
    private final String username;
    private final String languageCode;
    private Skin skin;

    private int chunkRadius;

    private final ImplPlayerAttributes attributes = new ImplPlayerAttributes();


    public ImplPlayer(ImplServer server, BedrockClientSession session, LoginPacket loginPacket) {
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
    public MinecraftVersion getVersion() {
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
    public Skin getSkin() {
        return this.skin;
    }

    @Override
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

    public ImplServer getServer() {
        return this.server;
    }

    @Override
    public void sendPacket(BedrockPacket packet) {
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
                    ImplChunk chunk = (ImplChunk)this.getLocation().getWorld().getChunkManager().getChunk(chunkX, chunkZ);
                    chunk.despawnFrom(this);
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
        return (int)attribute.getCurrentValue();
    }

    @Override
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
        super.onSpawned();
        this.sendNetworkChunkPublisher();   // Load chunks sent during initial login handshake

        this.updateVisibleChunks(null, this.chunkRadius);
        this.completeLogin();
    }

    private void completeLogin() {
        this.getMetaData().setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.HAS_GRAVITY, true);
        this.getMetaData().setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.IS_BREATHING, true);
        this.setMetaData(this.getMetaData());
        this.sendAttributes();

        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.setStatus(PlayStatusPacket.PlayStatus.PLAYER_SPAWN);
        this.sendPacket(playStatusPacket);
    }

    @Override
    public void spawnTo(Player player) {
        // TODO: implement in order for multiplayer to work properly
    }

    @Override
    public void requestSendChunk(int x, int z) {
        this.getLocation().getWorld().getChunkManager().sendPlayerChunkRequest(this, x, z);
    }

    private void sendNetworkChunkPublisher() {
        NetworkChunkPublisherUpdatePacket packet = new NetworkChunkPublisherUpdatePacket();
        packet.setCoordinates(((ImplLocation)this.getLocation()).toVector3i());
        packet.setRadius(this.getChunkRadius() * 16);
        this.sendPacket(packet);
    }

    /**
     * Sends and removes chunks the player can and cannot see
     */
    private void updateVisibleChunks(Location oldLocation, int oldChunkRadius) {
        Set<Tuple<Integer, Integer>> chunksToRemove = new HashSet<>();
        if (oldLocation != null) {
            // What were our previous chunks loaded?
            int oldPlayerChunkX = oldLocation.getChunkX();
            int oldPlayerChunkZ = oldLocation.getChunkZ();
            for (int x = -oldChunkRadius; x <= oldChunkRadius; x++) {
                for (int z = -oldChunkRadius; z <= oldChunkRadius; z++) {
                    // Chunk radius is ciruclar
                    int distance = (int)Math.round(Math.sqrt((x * x) + (z * z)));
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
                if (chunksToRemove.remove(new Tuple<>((currentPlayerChunkX + x), (currentPlayerChunkZ + z)))) {
                    continue;   // We don't need to send this chunk because it's already rendered to us
                }

                // Chunk radius is circular
                int distance = (int)Math.round(Math.sqrt((x * x) + (z * z)));
                if (this.getChunkRadius() > distance) {
                    this.requestSendChunk(currentPlayerChunkX + x, currentPlayerChunkZ + z);
                }
            }
        }

        // Remove each chunk we shouldn't get packets from
        for (Tuple<Integer, Integer> key : chunksToRemove) {
            ((ImplChunk)this.getLocation().getWorld().getChunkManager().getChunk(key.getObjectA(), key.getObjectB())).despawnFrom(this);
        }

        this.sendNetworkChunkPublisher();
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
        textPacket.setXuid(sender.getXuid());
        this.sendPacket(textPacket);
    }


}
