package io.github.pizzaserver.api.entity.meta;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataType;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;

/**
 * Entities have metadata which gives them special properties about how they are rendered to the Minecraft client.
 */
public interface EntityMetadata {

    boolean hasFlag(EntityFlag flag);

    EntityMetadata putFlag(EntityFlag flag, boolean enabled);

    boolean contains(EntityDataType<?> propertyName);

    byte getByte(EntityDataType<?> propertyName);

    EntityMetadata putByte(EntityDataType<?> propertyName, byte value);

    short getShort(EntityDataType<?> propertyName);

    EntityMetadata putShort(EntityDataType<?> propertyName, short value);

    int getInt(EntityDataType<?> propertyName);

    EntityMetadata putInt(EntityDataType<?> propertyName, int value);

    float getFloat(EntityDataType<?> propertyName);

    EntityMetadata putFloat(EntityDataType<?> propertyName, float value);

    long getLong(EntityDataType<?> propertyName);

    EntityMetadata putLong(EntityDataType<?> propertyName, long value);

    String getString(EntityDataType<?> propertyName);

    EntityMetadata putString(EntityDataType<?> propertyName, String value);

    NbtMap getNBT(EntityDataType<?> propertyName);

    EntityMetadata putNBT(EntityDataType<?> propertyName, NbtMap value);

    Vector3i getVector3i(EntityDataType<?> propertyName);

    EntityMetadata putVector3i(EntityDataType<?> propertyName, Vector3i value);

    Vector3f getVector3f(EntityDataType<?> propertyName);

    EntityMetadata putVector3f(EntityDataType<?> propertyName, Vector3f value);

}
