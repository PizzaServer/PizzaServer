package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.network.protocol.data.EntityLink;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

import java.util.Collections;
import java.util.Set;

public class AddEntityPacket extends BaseBedrockPacket {

    private long entityUniqueId;
    private long entityRuntimeId;
    private String entityType;

    private Vector3 position;
    private Vector3 velocity;

    private float pitch;
    private float yaw;
    private float headYaw;

    private EntityMetaData metaData;
    private Set<EntityLink> entityLinks = Collections.emptySet();

    public AddEntityPacket() {
        super(1);
    }

    public long getEntityUniqueId() {
        return this.entityUniqueId;
    }

    public void setEntityUniqueId(long entityUniqueId) {
        this.entityUniqueId = entityUniqueId;
    }

    public long getEntityRuntimeId() {
        return this.entityRuntimeId;
    }

    public void setEntityRuntimeId(long entityRuntimeId) {
        this.entityRuntimeId = entityRuntimeId;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
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
