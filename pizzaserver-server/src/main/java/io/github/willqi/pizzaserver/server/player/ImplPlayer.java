package io.github.willqi.pizzaserver.server.player;

import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.player.PlayerList;
import io.github.willqi.pizzaserver.api.player.attributes.Attribute;
import io.github.willqi.pizzaserver.api.player.attributes.PlayerAttributes;
import io.github.willqi.pizzaserver.api.player.skin.Skin;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.entity.BaseLivingEntity;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlagCategory;
import io.github.willqi.pizzaserver.server.network.BedrockClientSession;
import io.github.willqi.pizzaserver.server.network.protocol.data.MovementMode;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.api.player.attributes.AttributeType;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.willqi.pizzaserver.api.player.data.Device;
import io.github.willqi.pizzaserver.server.utils.ImplLocation;
import io.github.willqi.pizzaserver.server.world.chunks.ImplChunk;
import io.github.willqi.pizzaserver.server.world.chunks.ImplChunkManager;

import java.util.*;

public class ImplPlayer extends BaseLivingEntity implements Player {

    private final ImplServer server;
    private final BedrockClientSession session;

    private final BaseMinecraftVersion version;
    private final Device device;
    private final String xuid;
    private final UUID uuid;
    private final String username;
    private final String languageCode;

    private PlayerList playerList = new ImplPlayerList(this);
    private Skin skin;

    private int chunkRadius;

    private final PlayerAttributes attributes = new PlayerAttributes();


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
        // TODO: packet level stuff for player skin updates
        this.skin = newSkin;
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
    public PlayerList getPlayerList() {
        return this.playerList;
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
    public void moveTo(float x, float y, float z) {
        Location oldLocation = new ImplLocation(this.world, new Vector3(this.x, this.y, this.z));
        super.moveTo(x, y, z);

        if (!oldLocation.getChunk().equals(this.getChunk())) {
            this.updateVisibleChunks(oldLocation, this.getChunkRadius());
        }

        // TODO: movement should be every tick rather than whenever moveTo is called
        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
        movePlayerPacket.setEntityRuntimeId(this.getId());
        movePlayerPacket.setPosition(new Vector3(this.getX(), this.getY() + this.getEyeHeight(), this.getZ()));
        movePlayerPacket.setPitch(this.getPitch());
        movePlayerPacket.setYaw(this.getYaw());
        movePlayerPacket.setHeadYaw(this.getHeadYaw());
        movePlayerPacket.setMode(MovementMode.NORMAL);
        movePlayerPacket.setOnGround(false);    // TODO: does this change anything when sent to a client?
        for (Player player : this.getViewers()) {
            player.sendPacket(movePlayerPacket);
        }
    }

    @Override
    public void sendPacket(BaseBedrockPacket packet) {
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
            Location location = this.getLocation();

            // remove the player from the player list of others
            for (Player player : this.getServer().getPlayers()) {
                player.getPlayerList().removeEntry(this.getPlayerListEntry());
            }

            // Remove player entity from the world
            this.despawn();

            // Remove all of the chunks this player is viewing
            for (int chunkX = location.getChunkX() - this.getChunkRadius(); chunkX <= location.getChunkX() + this.getChunkRadius(); chunkX++) {
                for (int chunkZ = location.getChunkZ() - this.getChunkRadius(); chunkZ <= location.getChunkZ() + this.getChunkRadius(); chunkZ++) {
                    ImplChunk chunk = (ImplChunk)location.getWorld().getChunkManager().getChunk(chunkX, chunkZ);
                    chunk.despawnFrom(this);
                }
            }
        }
    }

    @Override
    public void onSpawned() {
        super.onSpawned();

        this.updateVisibleChunks(null, this.chunkRadius);

        this.getMetaData().setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.HAS_GRAVITY, true);
        this.getMetaData().setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.IS_BREATHING, true);
        this.setMetaData(this.getMetaData());
        this.sendAttributes();

        // Update every other player's player list to include this player
        for (Player player : this.getServer().getPlayers()) {
            if (!this.isHiddenFrom(player) && !player.equals(this)) {
                player.getPlayerList().addEntry(this.getPlayerListEntry());
            }
        }

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
        player.sendPacket(addPlayerPacket);
    }

    @Override
    public void sendChunk(int x, int z) {
        ImplChunkManager chunkManager = (ImplChunkManager)this.getLocation().getWorld().getChunkManager();
        if (chunkManager.isChunkLoaded(x, z)) {
            chunkManager.addChunkToPlayerQueue(this, (ImplChunk)chunkManager.getChunk(x, z));
        } else {
            chunkManager.fetchChunk(x, z).whenComplete((chunk, exception) -> {
                if (exception != null) {
                    ImplServer.getInstance().getLogger().error("Failed to send chunk (" + x + ", " + z + ") to player " + this.getUsername(), exception);
                    return;
                }
                chunkManager.addChunkToPlayerQueue(this, (ImplChunk)chunk);
            });
        }
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
        Set<ImplChunk> chunksToRemove = new HashSet<>();

        if (oldLocation != null) {
            // What were our previous chunks loaded?
            for (int chunkX = oldLocation.getChunkX() - oldChunkRadius; chunkX <= oldLocation.getChunkX() + oldChunkRadius; chunkX++) {
                for (int chunkZ = oldLocation.getChunkZ() - oldChunkRadius; chunkZ <= oldLocation.getChunkZ() + oldChunkRadius; chunkZ++) {
                    if (oldLocation.getWorld().getChunkManager().isChunkLoaded(chunkX, chunkZ)) {
                        ImplChunk chunk = (ImplChunk)oldLocation.getWorld().getChunkManager().getChunk(chunkX, chunkZ);
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
                    ImplChunk chunk = (ImplChunk)this.getLocation().getWorld().getChunkManager().getChunk(chunkX, chunkZ);
                    if (chunksToRemove.remove(chunk)) {
                        continue;   // We don't need to send this chunk
                    }
                }
                requiresChunkPublisher = true;
                this.sendChunk(chunkX, chunkZ);
            }
        }

        // Remove each chunk we shouldn't get packets from
        for (ImplChunk chunk : chunksToRemove) {
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
    public void sendPlayerMessage(Player sender, String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setType(TextPacket.TextType.CHAT);
        textPacket.setSourceName(sender.getUsername());
        textPacket.setMessage(message);
        textPacket.setXuid(sender.getXUID());
        this.sendPacket(textPacket);
    }

}
