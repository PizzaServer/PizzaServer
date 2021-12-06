package io.github.pizzaserver.server.entity;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.data.entity.EntityData;
import com.nukkitx.protocol.bedrock.data.entity.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlag;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlags;
import com.nukkitx.protocol.bedrock.packet.SetEntityDataPacket;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.meta.EntityMetadata;
import io.github.pizzaserver.api.player.Player;

public class ImplEntityMetadata implements EntityMetadata {

    private final Entity entity;

    private final EntityDataMap dataMap = new EntityDataMap();
    private boolean updated;


    public ImplEntityMetadata(Entity entity) {
        this.entity = entity;
    }

    @Override
    public boolean hasFlag(EntityFlag flag) {
        return this.dataMap.getOrCreateFlags().getFlag(flag);
    }

    @Override
    public EntityMetadata putFlag(EntityFlag flag, boolean enabled) {
        EntityFlags flags = this.dataMap.getOrCreateFlags();
        flags.setFlag(flag, enabled);
        this.dataMap.putFlags(flags);
        this.updated = true;
        return this;
    }

    @Override
    public boolean contains(EntityData propertyName) {
        return this.dataMap.containsKey(propertyName);
    }

    @Override
    public byte getByte(EntityData propertyName) {
        return this.dataMap.getByte(propertyName, (byte) 0);
    }

    @Override
    public EntityMetadata putByte(EntityData propertyName, byte value) {
        this.dataMap.putByte(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public short getShort(EntityData propertyName) {
        return this.dataMap.getShort(propertyName, (short) 0);
    }

    @Override
    public EntityMetadata putShort(EntityData propertyName, short value) {
        this.dataMap.putShort(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public int getInt(EntityData propertyName) {
        return this.dataMap.getInt(propertyName, 0);
    }

    @Override
    public EntityMetadata putInt(EntityData propertyName, int value) {
        this.dataMap.putInt(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public float getFloat(EntityData propertyName) {
        return this.dataMap.getFloat(propertyName, 0);
    }

    @Override
    public EntityMetadata putFloat(EntityData propertyName, float value) {
        this.dataMap.putFloat(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public long getLong(EntityData propertyName) {
        return this.dataMap.getLong(propertyName, 0);
    }

    @Override
    public EntityMetadata putLong(EntityData propertyName, long value) {
        this.dataMap.putLong(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public String getString(EntityData propertyName) {
        return this.dataMap.getString(propertyName, null);
    }

    @Override
    public EntityMetadata putString(EntityData propertyName, String value) {
        this.dataMap.putString(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public NbtMap getNBT(EntityData propertyName) {
        return this.dataMap.getTag(propertyName, null);
    }

    @Override
    public EntityMetadata putNBT(EntityData propertyName, NbtMap value) {
        this.dataMap.putTag(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public Vector3i getVector3i(EntityData propertyName) {
        return this.dataMap.getPos(propertyName, null);
    }

    public EntityMetadata putVector3i(EntityData propertyName, Vector3i value) {
        this.dataMap.putPos(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public Vector3f getVector3f(EntityData propertyName) {
        return this.dataMap.getVector3f(propertyName);
    }

    @Override
    public EntityMetadata putVector3f(EntityData propertyName, Vector3f value) {
        this.dataMap.putVector3f(propertyName, value);
        this.updated = true;
        return this;
    }

    public void tryUpdate() {
        if (this.updated) {
            this.updated = false;

            SetEntityDataPacket entityDataPacket = new SetEntityDataPacket();
            entityDataPacket.setRuntimeEntityId(this.entity.getId());
            entityDataPacket.getMetadata().putAll(this.dataMap);
            for (Player player : this.entity.getViewers()) {
                player.sendPacket(entityDataPacket);
            }
            if (this.entity instanceof Player) {
                ((Player) this.entity).sendPacket(entityDataPacket);
            }
        }
    }

    public EntityDataMap serialize() {
        return this.dataMap;
    }

}
