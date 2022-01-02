package io.github.pizzaserver.api.entity.meta;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.data.entity.EntityData;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlag;

/**
 * Entities have metadata which gives them special properties about how they are rendered to the Minecraft client.
 */
public interface EntityMetadata {

    boolean hasFlag(EntityFlag flag);

    EntityMetadata putFlag(EntityFlag flag, boolean enabled);

    boolean contains(EntityData propertyName);

    byte getByte(EntityData propertyName);

    EntityMetadata putByte(EntityData propertyName, byte value);

    short getShort(EntityData propertyName);

    EntityMetadata putShort(EntityData propertyName, short value);

    int getInt(EntityData propertyName);

    EntityMetadata putInt(EntityData propertyName, int value);

    float getFloat(EntityData propertyName);

    EntityMetadata putFloat(EntityData propertyName, float value);

    long getLong(EntityData propertyName);

    EntityMetadata putLong(EntityData propertyName, long value);

    String getString(EntityData propertyName);

    EntityMetadata putString(EntityData propertyName, String value);

    NbtMap getNBT(EntityData propertyName);

    EntityMetadata putNBT(EntityData propertyName, NbtMap value);

    Vector3i getVector3i(EntityData propertyName);

    EntityMetadata putVector3i(EntityData propertyName, Vector3i value);

    Vector3f getVector3f(EntityData propertyName);

    EntityMetadata putVector3f(EntityData propertyName, Vector3f value);

}
