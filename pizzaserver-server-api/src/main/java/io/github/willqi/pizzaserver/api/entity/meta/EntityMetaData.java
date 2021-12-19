package io.github.willqi.pizzaserver.api.entity.meta;

import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlagCategory;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaProperty;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyType;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.util.*;

/**
 * Entities have metadata which gives them special properties about how they are rendered to the Minecraft client.
 */
public class EntityMetaData {

    private final Map<EntityMetaFlagCategory, Set<EntityMetaFlag>> flags = new HashMap<>();
    private final Map<EntityMetaPropertyName, EntityMetaProperty<?>> properties = new HashMap<>();


    /**
     * Retrieve all enabled entity flags.
     * @return all enabled entity flags
     */
    public Map<EntityMetaFlagCategory, Set<EntityMetaFlag>> getFlags() {
        return this.flags;
    }

    /**
     * Check if the metadata has {@link EntityMetaFlag} enabled under the {@link EntityMetaFlagCategory}.
     * @param flagCategory the category the flag falls under
     * @param flag the flag to check
     * @return if the flag is enabled
     */
    public boolean hasFlag(EntityMetaFlagCategory flagCategory, EntityMetaFlag flag) {
        if (!this.flags.containsKey(flagCategory)) {
            return false;
        }
        return this.flags.get(flagCategory).contains(flag);
    }

    /**
     * Change the status of a {@link EntityMetaFlag} under a {@link EntityMetaFlagCategory}.
     * @param flagCategory the category the flag falls under
     * @param flag the flag to check
     * @param enabled if the flag should be enabled
     */
    public void setFlag(EntityMetaFlagCategory flagCategory, EntityMetaFlag flag, boolean enabled) {
        if (!this.flags.containsKey(flagCategory)) {
            this.flags.put(flagCategory, new HashSet<>());
        }

        if (enabled) {
            this.flags.get(flagCategory).add(flag);
        } else {
            this.flags.get(flagCategory).remove(flag);
        }
    }

    /**
     * Get all entity data properties set.
     * @return a map of all properties
     */
    public Map<EntityMetaPropertyName, EntityMetaProperty<?>> getProperties() {
        return Collections.unmodifiableMap(this.properties);
    }

    /**
     * Check if the metadata has a property set.
     * @param propertyName the {@link EntityMetaPropertyName} we are checking
     * @return if it is set
     */
    public boolean hasProperty(EntityMetaPropertyName propertyName) {
        return this.properties.containsKey(propertyName);
    }

    public byte getByteProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return 0;
        }

        return ((Byte) this.getProperty(propertyName, EntityMetaPropertyType.BYTE).getValue());
    }

    public void setByteProperty(EntityMetaPropertyName propertyName, byte value) {
        this.setProperty(propertyName, new EntityMetaProperty<>(EntityMetaPropertyType.BYTE, value));
    }

    public short getShortProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return 0;
        }

        return ((Short) this.getProperty(propertyName, EntityMetaPropertyType.SHORT).getValue());
    }

    public void setShortProperty(EntityMetaPropertyName propertyName, short value) {
        this.setProperty(propertyName, new EntityMetaProperty<>(EntityMetaPropertyType.SHORT, value));
    }

    public int getIntProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return 0;
        }

        return ((Integer) this.getProperty(propertyName, EntityMetaPropertyType.INTEGER).getValue());
    }

    public void setIntProperty(EntityMetaPropertyName propertyName, int value) {
        this.setProperty(propertyName, new EntityMetaProperty<>(EntityMetaPropertyType.INTEGER, value));
    }

    public float getFloatProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return 0;
        }

        return ((Float) this.getProperty(propertyName, EntityMetaPropertyType.FLOAT).getValue());
    }

    public void setFloatProperty(EntityMetaPropertyName propertyName, float value) {
        this.setProperty(propertyName, new EntityMetaProperty<>(EntityMetaPropertyType.FLOAT, value));
    }

    public long getLongProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return 0;
        }

        return ((Long) this.getProperty(propertyName, EntityMetaPropertyType.LONG).getValue());
    }

    public void setLongProperty(EntityMetaPropertyName propertyName, long value) {
        this.setProperty(propertyName, new EntityMetaProperty<>(EntityMetaPropertyType.LONG, value));
    }

    public String getStringProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return null;
        }

        return ((String) this.getProperty(propertyName, EntityMetaPropertyType.STRING).getValue());
    }

    public void setStringProperty(EntityMetaPropertyName propertyName, String value) {
        this.setProperty(propertyName, new EntityMetaProperty<>(EntityMetaPropertyType.STRING, value));
    }

    public NBTCompound getNBTProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return null;
        }

        return ((NBTCompound) this.getProperty(propertyName, EntityMetaPropertyType.NBT).getValue());
    }

    public void setNBTProperty(EntityMetaPropertyName propertyName, NBTCompound value) {
        this.setProperty(propertyName, new EntityMetaProperty<>(EntityMetaPropertyType.NBT, value));
    }

    public Vector3i getVector3iProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return null;
        }

        return ((Vector3i) this.getProperty(propertyName, EntityMetaPropertyType.VECTOR3I).getValue());
    }

    public void setVector3iProperty(EntityMetaPropertyName propertyName, Vector3i value) {
        this.setProperty(propertyName, new EntityMetaProperty<>(EntityMetaPropertyType.VECTOR3I, value));
    }

    public Vector3 getVector3Property(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return null;
        }

        return ((Vector3) this.getProperty(propertyName, EntityMetaPropertyType.VECTOR3).getValue());
    }

    public void setVector3Property(EntityMetaPropertyName propertyName, Vector3 value) {
        this.setProperty(propertyName, new EntityMetaProperty<>(EntityMetaPropertyType.VECTOR3, value));
    }

    private EntityMetaProperty<?> getProperty(EntityMetaPropertyName propertyName, EntityMetaPropertyType expectedType) {
        EntityMetaProperty<?> storedProperty = this.properties.get(propertyName);
        if (storedProperty.getType() != expectedType) {
            throw new IllegalArgumentException(propertyName + " was not a " + expectedType + " property.");
        }
        return storedProperty;
    }

    private void setProperty(EntityMetaPropertyName propertyName, EntityMetaProperty<?> property) {
        if (property.getType() != propertyName.getType()) {
            throw new IllegalArgumentException("The property provided was not of the type " + propertyName.getType());
        }
        this.properties.put(propertyName, property);
    }

}
