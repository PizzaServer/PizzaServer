package io.github.pizzaserver.api.entity.meta;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.data.entity.EntityData;
import com.nukkitx.protocol.bedrock.data.entity.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlag;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlags;

/**
 * Entities have metadata which gives them special properties about how they are rendered to the Minecraft client.
 */
public class EntityMetaData {

    private final EntityDataMap dataMap = new EntityDataMap();

    public boolean hasFlag(EntityFlag flag) {
        return this.dataMap.getOrCreateFlags().getFlag(flag);
    }

    public EntityMetaData putFlag(EntityFlag flag, boolean enabled) {
        EntityFlags flags = this.dataMap.getOrCreateFlags();
        flags.setFlag(flag, enabled);
        this.dataMap.putFlags(flags);
        return this;
    }

    public boolean contains(EntityData propertyName) {
        return this.dataMap.containsKey(propertyName);
    }

    public byte getByte(EntityData propertyName) {
        return this.dataMap.getByte(propertyName, (byte) 0);
    }

    public EntityMetaData putByte(EntityData propertyName, byte value) {
        this.dataMap.putByte(propertyName, value);
        return this;
    }

    public short getShort(EntityData propertyName) {
        return this.dataMap.getShort(propertyName, (short) 0);
    }

    public EntityMetaData putShort(EntityData propertyName, short value) {
        this.dataMap.putShort(propertyName, value);
        return this;
    }

    public int getInt(EntityData propertyName) {
        return this.dataMap.getInt(propertyName, 0);
    }

    public EntityMetaData putInt(EntityData propertyName, int value) {
        this.dataMap.putInt(propertyName, value);
        return this;
    }

    public float getFloat(EntityData propertyName) {
        return this.dataMap.getFloat(propertyName, 0);
    }

    public EntityMetaData putFloat(EntityData propertyName, float value) {
        this.dataMap.putFloat(propertyName, value);
        return this;
    }

    public long getLong(EntityData propertyName) {
        return this.dataMap.getLong(propertyName, 0);
    }

    public EntityMetaData putLong(EntityData propertyName, long value) {
        this.dataMap.putLong(propertyName, value);
        return this;
    }

    public String getString(EntityData propertyName) {
        return this.dataMap.getString(propertyName, null);
    }

    public EntityMetaData putString(EntityData propertyName, String value) {
        this.dataMap.putString(propertyName, value);
        return this;
    }

    public NbtMap getNBT(EntityData propertyName) {
        return this.dataMap.getTag(propertyName, null);
    }

    public EntityMetaData putNBT(EntityData propertyName, NbtMap value) {
        this.dataMap.putTag(propertyName, value);
        return this;
    }

    public Vector3i getVector3i(EntityData propertyName) {
        return this.dataMap.getPos(propertyName, null);
    }

    public EntityMetaData putVector3i(EntityData propertyName, Vector3i value) {
        this.dataMap.putPos(propertyName, value);
        return this;
    }

    public Vector3f getVector3f(EntityData propertyName) {
        return this.dataMap.getVector3f(propertyName);
    }

    public EntityMetaData putVector3f(EntityData propertyName, Vector3f value) {
        this.dataMap.putVector3f(propertyName, value);
        return this;
    }

    public EntityDataMap serialize() {
        return this.dataMap;
    }

}
