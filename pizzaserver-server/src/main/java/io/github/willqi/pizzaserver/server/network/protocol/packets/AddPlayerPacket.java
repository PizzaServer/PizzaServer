package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.player.data.Device;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.network.protocol.data.EntityLink;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class AddPlayerPacket extends BaseBedrockPacket {

    public static final int ID = 0x0c;

    private UUID uuid;
    private String username;
    private String deviceId;
    private Device device;
    private float playerUniqueId;
    private String platformChatId = "";
    // TODO: When adventure settings are implemented we require the commandLevel, customLevel, and settings here.


    private long entityUniqueId;
    private long entityRuntimeId;
    private Vector3 position;
    private float pitch;
    private float yaw;
    private float headYaw;
    private Vector3 velocity;
    private EntityMetaData metaData;
    private Set<EntityLink> entityLinks = Collections.emptySet();
    // TODO: When items are implemented we require the held item


    public AddPlayerPacket() {
        super(ID);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public float getPlayerUniqueId() {
        return this.playerUniqueId;
    }

    public void setPlayerUniqueId(long uniqueId) {
        this.playerUniqueId = uniqueId;
    }

    public String getPlatformChatId() {
        return this.platformChatId;
    }

    public void setPlatformChatId(String platformChatId) {
        this.platformChatId = platformChatId;
    }

    public long getEntityUniqueId() {
        return this.entityUniqueId;
    }

    public void setEntityUniqueId(long uniqueId) {
        this.entityUniqueId = uniqueId;
    }

    public long getEntityRuntimeId() {
        return this.entityRuntimeId;
    }

    public void setEntityRuntimeId(long runtimeId) {
        this.entityRuntimeId = runtimeId;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getHeadYaw() {
        return this.headYaw;
    }

    public void setHeadYaw(float headYaw) {
        this.headYaw = headYaw;
    }

    public Vector3 getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

    public EntityMetaData getMetaData() {
        return this.metaData;
    }

    public void setMetaData(EntityMetaData metaData) {
        this.metaData = metaData;
    }

    public Set<EntityLink> getEntityLinks() {
        return this.entityLinks;
    }

    public void setEntityLinks(Set<EntityLink> links) {
        this.entityLinks = links;
    }



}
