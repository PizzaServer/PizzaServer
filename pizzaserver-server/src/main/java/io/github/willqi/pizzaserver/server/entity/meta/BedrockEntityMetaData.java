package io.github.willqi.pizzaserver.server.entity.meta;

import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaProperty;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlagCategory;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.server.entity.meta.properties.BedrockEntityMetaProperty;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyType;

import java.util.*;

public class BedrockEntityMetaData implements EntityMetaData {

    private final Map<EntityMetaFlagCategory, Set<EntityMetaFlag>> flags = new HashMap<>();
    private final Map<EntityMetaPropertyName, BedrockEntityMetaProperty<?>> properties = new HashMap<>();


    @Override
    public Map<EntityMetaFlagCategory, Set<EntityMetaFlag>> getFlags() {
        return this.flags;
    }

    @Override
    public boolean hasFlag(EntityMetaFlagCategory flagCategory, EntityMetaFlag flag) {
        if (!this.flags.containsKey(flagCategory)) {
            return false;
        }
        return this.flags.get(flagCategory).contains(flag);
    }

    @Override
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

    @Override
    public Map<EntityMetaPropertyName, EntityMetaProperty<?>> getProperties() {
        return Collections.unmodifiableMap(this.properties);
    }

    @Override
    public boolean hasProperty(EntityMetaPropertyName propertyName) {
        return this.properties.containsKey(propertyName);
    }

    @Override
    public byte getByteProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return 0;
        }

        return ((byte)this.getProperty(propertyName, EntityMetaPropertyType.BYTE).getValue());
    }

    @Override
    public void setByteProperty(EntityMetaPropertyName propertyName, byte value) {
        this.setProperty(propertyName, new BedrockEntityMetaProperty<>(EntityMetaPropertyType.BYTE, value));
    }

    @Override
    public short getShortProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return 0;
        }

        return ((short)this.getProperty(propertyName, EntityMetaPropertyType.SHORT).getValue());
    }

    @Override
    public void setShortProperty(EntityMetaPropertyName propertyName, short value) {
        this.setProperty(propertyName, new BedrockEntityMetaProperty<>(EntityMetaPropertyType.SHORT, value));
    }

    @Override
    public int getIntProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return 0;
        }

        return ((int)this.getProperty(propertyName, EntityMetaPropertyType.INTEGER).getValue());
    }

    @Override
    public void setIntProperty(EntityMetaPropertyName propertyName, int value) {
        this.setProperty(propertyName, new BedrockEntityMetaProperty<>(EntityMetaPropertyType.INTEGER, value));
    }

    @Override
    public float getFloatProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return 0;
        }

        return ((float)this.getProperty(propertyName, EntityMetaPropertyType.FLOAT).getValue());
    }

    @Override
    public void setFloatProperty(EntityMetaPropertyName propertyName, float value) {
        this.setProperty(propertyName, new BedrockEntityMetaProperty<>(EntityMetaPropertyType.FLOAT, value));
    }

    @Override
    public long getLongProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return 0;
        }

        return ((long)this.getProperty(propertyName, EntityMetaPropertyType.LONG).getValue());
    }

    @Override
    public void setLongProperty(EntityMetaPropertyName propertyName, long value) {
        this.setProperty(propertyName, new BedrockEntityMetaProperty<>(EntityMetaPropertyType.LONG, value));
    }

    @Override
    public String getStringProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return null;
        }

        return ((String)this.getProperty(propertyName, EntityMetaPropertyType.STRING).getValue());
    }

    @Override
    public void setStringProperty(EntityMetaPropertyName propertyName, String value) {
        this.setProperty(propertyName, new BedrockEntityMetaProperty<>(EntityMetaPropertyType.STRING, value));
    }

    @Override
    public NBTCompound getNBTProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return null;
        }

        return ((NBTCompound)this.getProperty(propertyName, EntityMetaPropertyType.NBT).getValue());
    }

    @Override
    public void setNBTProperty(EntityMetaPropertyName propertyName, NBTCompound value) {
        this.setProperty(propertyName, new BedrockEntityMetaProperty<>(EntityMetaPropertyType.NBT, value));
    }

    @Override
    public Vector3i getVector3iProperty(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return null;
        }

        return ((Vector3i)this.getProperty(propertyName, EntityMetaPropertyType.VECTOR3I).getValue());
    }

    @Override
    public void setVector3iProperty(EntityMetaPropertyName propertyName, Vector3i value) {
        this.setProperty(propertyName, new BedrockEntityMetaProperty<>(EntityMetaPropertyType.VECTOR3I, value));
    }

    @Override
    public Vector3 getVector3Property(EntityMetaPropertyName propertyName) {
        if (!this.hasProperty(propertyName)) {
            return null;
        }

        return ((Vector3)this.getProperty(propertyName, EntityMetaPropertyType.VECTOR3).getValue());
    }

    @Override
    public void setVector3Property(EntityMetaPropertyName propertyName, Vector3 value) {
        this.setProperty(propertyName, new BedrockEntityMetaProperty<>(EntityMetaPropertyType.VECTOR3, value));
    }

    private BedrockEntityMetaProperty<?> getProperty(EntityMetaPropertyName propertyName, EntityMetaPropertyType expectedType) {
        BedrockEntityMetaProperty<?> storedProperty = this.properties.get(propertyName);
        if (storedProperty.getType() != expectedType) {
            throw new IllegalArgumentException(propertyName + " was not a " + expectedType + " property.");
        }
        return storedProperty;
    }

    private void setProperty(EntityMetaPropertyName propertyName, BedrockEntityMetaProperty<?> property) {
        if (property.getType() != propertyName.getType()) {
            throw new IllegalArgumentException("The property provided was not of the type " + propertyName.getType());
        }
        this.properties.put(propertyName, property);
    }

}
