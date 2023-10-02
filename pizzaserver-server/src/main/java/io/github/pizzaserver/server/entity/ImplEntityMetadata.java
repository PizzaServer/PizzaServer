package io.github.pizzaserver.server.entity;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.meta.EntityMetadata;
import io.github.pizzaserver.api.player.Player;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataType;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityDataPacket;

import java.util.EnumSet;

public class ImplEntityMetadata implements EntityMetadata {

    private final Entity entity;

    private final EntityDataMap dataMap = new EntityDataMap();
    private boolean updated;


    public ImplEntityMetadata(Entity entity) {
        this.entity = entity;
    }

    @Override
    public boolean hasFlag(EntityFlag flag) {
        return this.dataMap.getOrCreateFlags().contains(flag);
    }

    @Override
    public EntityMetadata putFlag(EntityFlag flag, boolean enabled) {
        EnumSet<EntityFlag> flags = this.dataMap.getOrCreateFlags();
        boolean toUpdate = (flags.contains(flag) && !enabled) || (!flags.contains(flag) && enabled);
        if (enabled && toUpdate) {
            flags.add(flag);
        } else if(!enabled && toUpdate) {
            flags.remove(flag);
        }
        // TODO: This makes me feel weird
        this.dataMap.setFlag(flag, enabled);
        this.updated |= toUpdate;
        this.dataMap.putFlags(flags);
        return this;
    }

    @Override
    public boolean contains(EntityDataType<?> propertyName) {
        return this.dataMap.containsKey(propertyName);
    }

    @Override
    public byte getByte(EntityDataType<?> propertyName) {
        Object result = this.dataMap.get(propertyName);
        return result != null ? (byte) result : 0;
    }

    @Override
    public EntityMetadata putByte(EntityDataType<?> propertyName, byte value) {
        this.dataMap.put(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public short getShort(EntityDataType<?> propertyName) {
        Object result = this.dataMap.get(propertyName);
        return result != null ? (short) result : 0;
    }

    @Override
    public EntityMetadata putShort(EntityDataType<?> propertyName, short value) {
        this.dataMap.put(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public int getInt(EntityDataType<?> propertyName) {
        Object result = this.dataMap.get(propertyName);
        return result == null ? 0 : (int) result;
    }

    @Override
    public EntityMetadata putInt(EntityDataType<?> propertyName, int value) {
        this.dataMap.put(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public float getFloat(EntityDataType<?> propertyName) {
        Object result = this.dataMap.get(propertyName);
        return result == null ? 0 : (float) result;
    }

    @Override
    public EntityMetadata putFloat(EntityDataType<?> propertyName, float value) {
        this.dataMap.put(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public long getLong(EntityDataType<?> propertyName) {
        Object result = this.dataMap.get(propertyName);
        return result == null ? 0 : (long) result;
    }

    @Override
    public EntityMetadata putLong(EntityDataType<?> propertyName, long value) {
        this.dataMap.put(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public String getString(EntityDataType<?> propertyName) {
        Object result = this.dataMap.get(propertyName);
        return result == null ? "" : (String) result;
    }

    @Override
    public EntityMetadata putString(EntityDataType<?> propertyName, String value) {
        this.dataMap.put(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public NbtMap getNBT(EntityDataType<?> propertyName) {
        Object result = this.dataMap.get(propertyName);
        return result == null ? NbtMap.EMPTY : (NbtMap) result;
    }

    @Override
    public EntityMetadata putNBT(EntityDataType<?> propertyName, NbtMap value) {
        this.dataMap.put(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public Vector3i getVector3i(EntityDataType<?> propertyName) {
        Object result = this.dataMap.get(propertyName);
        return result == null ? Vector3i.ZERO : (Vector3i) result;
    }

    public EntityMetadata putVector3i(EntityDataType<?> propertyName, Vector3i value) {
        this.dataMap.put(propertyName, value);
        this.updated = true;
        return this;
    }

    @Override
    public Vector3f getVector3f(EntityDataType<?> propertyName) {
        Object result = this.dataMap.get(propertyName);
        return result == null ? Vector3f.ZERO : (Vector3f) result;
    }

    @Override
    public EntityMetadata putVector3f(EntityDataType<?> propertyName, Vector3f value) {
        this.dataMap.put(propertyName, value);
        this.updated = true;
        return this;
    }

    public void tryUpdate() {
        if (this.updated) {
            this.updated = false;

            this.update();
        }
    }

    public void update() {
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

    public EntityDataMap serialize() {
        return this.dataMap;
    }

}
