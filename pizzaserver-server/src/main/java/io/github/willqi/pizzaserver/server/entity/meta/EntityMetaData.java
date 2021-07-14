package io.github.willqi.pizzaserver.server.entity.meta;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.server.entity.meta.flags.EntityMetaFlagType;
import io.github.willqi.pizzaserver.server.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.server.entity.meta.properties.EntityMetaProperty;
import io.github.willqi.pizzaserver.server.entity.meta.properties.EntityMetaPropertyType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityMetaData {

    private final Map<EntityMetaFlagType, Set<EntityMetaFlag>> flags = new HashMap<>();
    private final Map<EntityMetaPropertyName, EntityMetaProperty<?>> properties = new HashMap<>();


    public Map<EntityMetaFlagType, Set<EntityMetaFlag>> getFlags() {
        return this.flags;
    }

    public boolean hasFlag(EntityMetaFlagType flagType, EntityMetaFlag flag) {
        if (!this.flags.containsKey(flagType)) {
            return false;
        }
        return this.flags.get(flagType).contains(flag);
    }

    public void setFlag(EntityMetaFlagType flagType, EntityMetaFlag flag, boolean status) {
        if (!this.flags.containsKey(flagType)) {
            this.flags.put(flagType, new HashSet<>());
        }

        if (status) {
            this.flags.get(flagType).add(flag);
        } else {
            this.flags.get(flagType).remove(flag);
        }
    }

    public Map<EntityMetaPropertyName, EntityMetaProperty<?>> getProperties() {
        return this.properties;
    }

    public boolean hasProperty(EntityMetaPropertyName propertyName) {
        return this.properties.containsKey(propertyName);
    }

    public void setProperty(EntityMetaPropertyName propertyName, EntityMetaProperty<?> property) {
        if (property.getType() != propertyName.getType()) {
            throw new IllegalArgumentException("The property provided was not of the type " + propertyName.getType());
        }
        this.properties.put(propertyName, property);
    }

    public byte getByteProperty(EntityMetaPropertyName property) {
        if (!this.hasProperty(property)) {
            return (byte)0;
        }

        EntityMetaProperty<Byte> storedProperty = (EntityMetaProperty<Byte>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.BYTE) {
            throw new IllegalArgumentException(property + " was not a byte property.");
        }
        return storedProperty.getValue();
    }

    public short getShortProperty(EntityMetaPropertyName property) {
        if (!this.hasProperty(property)) {
            return 0;
        }

        EntityMetaProperty<Short> storedProperty = (EntityMetaProperty<Short>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.SHORT) {
            throw new IllegalArgumentException(property + " was not a short property.");
        }
        return storedProperty.getValue();
    }

    public int getIntProperty(EntityMetaPropertyName property) {
        if (!this.hasProperty(property)) {
            return 0;
        }

        EntityMetaProperty<Integer> storedProperty = (EntityMetaProperty<Integer>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.INTEGER) {
            throw new IllegalArgumentException(property + " was not a integer property.");
        }
        return storedProperty.getValue();
    }

    public float getFloatProperty(EntityMetaPropertyName property) {
        if (!this.hasProperty(property)) {
            return 0;
        }

        EntityMetaProperty<Float> storedProperty = (EntityMetaProperty<Float>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.FLOAT) {
            throw new IllegalArgumentException(property + " was not a float property.");
        }
        return storedProperty.getValue();
    }

    public long getLongProperty(EntityMetaPropertyName property) {
        if (!this.hasProperty(property)) {
            return 0;
        }

        EntityMetaProperty<Long> storedProperty = (EntityMetaProperty<Long>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.LONG) {
            throw new IllegalArgumentException(property + " was not a long property.");
        }
        return storedProperty.getValue();
    }

    public String getStringProperty(EntityMetaPropertyName property) {
        if (!this.hasProperty(property)) {
            return null;
        }

        EntityMetaProperty<String> storedProperty = (EntityMetaProperty<String>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.STRING) {
            throw new IllegalArgumentException(property + " was not a string property.");
        }
        return storedProperty.getValue();
    }

    public NBTCompound getNBTProperty(EntityMetaPropertyName property) {
        if (!this.hasProperty(property)) {
            return null;
        }

        EntityMetaProperty<NBTCompound> storedProperty = (EntityMetaProperty<NBTCompound>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.NBT) {
            throw new IllegalArgumentException(property + " was not a NBT property.");
        }
        return storedProperty.getValue();
    }

    public Vector3i getVector3iProperty(EntityMetaPropertyName property) {
        if (!this.hasProperty(property)) {
            return null;
        }

        EntityMetaProperty<Vector3i> storedProperty = (EntityMetaProperty<Vector3i>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.VECTOR3I) {
            throw new IllegalArgumentException(property + " was not a Vector3i property.");
        }
        return storedProperty.getValue();
    }

    public Vector3 getVector3Property(EntityMetaPropertyName property) {
        if (!this.hasProperty(property)) {
            return null;
        }

        EntityMetaProperty<Vector3> storedProperty = (EntityMetaProperty<Vector3>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.VECTOR3) {
            throw new IllegalArgumentException(property + " was not a Vector3 property.");
        }
        return storedProperty.getValue();
    }


}
