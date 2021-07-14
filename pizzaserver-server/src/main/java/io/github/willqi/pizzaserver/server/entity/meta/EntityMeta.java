package io.github.willqi.pizzaserver.server.entity.meta;

import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.server.entity.meta.properties.EntityMetaProperty;
import io.github.willqi.pizzaserver.server.entity.meta.properties.EntityMetaPropertyType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityMeta {

    private final Set<EntityMetaFlag> flags = new HashSet<>();
    private final Map<EntityMetaPropertyName, EntityMetaProperty<?>> properties = new HashMap<>();


    public boolean hasFlag(EntityMetaFlag flag) {
        return this.flags.contains(flag);
    }

    public void setFlag(EntityMetaFlag flag, boolean status) {
        if (status) {
            this.flags.add(flag);
        } else {
            this.flags.remove(flag);
        }
    }

    public boolean hasProperty(EntityMetaPropertyName propertyName) {
        return this.properties.containsKey(propertyName);
    }

    public void setProperty(EntityMetaPropertyName propertyName, EntityMetaProperty<?> property) {
        this.properties.put(propertyName, property);
    }

    public byte getByteProperty(EntityMetaPropertyName property) {
        EntityMetaProperty<Byte> storedProperty = (EntityMetaProperty<Byte>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.BYTE) {
            throw new IllegalArgumentException(property + " was not a byte property.");
        }
        return storedProperty.getValue();
    }

    public short getShortProperty(EntityMetaPropertyName property) {
        EntityMetaProperty<Short> storedProperty = (EntityMetaProperty<Short>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.SHORT) {
            throw new IllegalArgumentException(property + " was not a short property.");
        }
        return storedProperty.getValue();
    }

    public int getIntProperty(EntityMetaPropertyName property) {
        EntityMetaProperty<Integer> storedProperty = (EntityMetaProperty<Integer>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.INTEGER) {
            throw new IllegalArgumentException(property + " was not a integer property.");
        }
        return storedProperty.getValue();
    }

    public float getFloatProperty(EntityMetaPropertyName property) {
        EntityMetaProperty<Float> storedProperty = (EntityMetaProperty<Float>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.FLOAT) {
            throw new IllegalArgumentException(property + " was not a float property.");
        }
        return storedProperty.getValue();
    }

    public long getLongProperty(EntityMetaPropertyName property) {
        EntityMetaProperty<Long> storedProperty = (EntityMetaProperty<Long>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.LONG) {
            throw new IllegalArgumentException(property + " was not a long property.");
        }
        return storedProperty.getValue();
    }

    public String getStringProperty(EntityMetaPropertyName property) {
        EntityMetaProperty<String> storedProperty = (EntityMetaProperty<String>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.STRING) {
            throw new IllegalArgumentException(property + " was not a string property.");
        }
        return storedProperty.getValue();
    }

    public NBTCompound getNBTProperty(EntityMetaPropertyName property) {
        EntityMetaProperty<NBTCompound> storedProperty = (EntityMetaProperty<NBTCompound>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.NBT) {
            throw new IllegalArgumentException(property + " was not a NBT property.");
        }
        return storedProperty.getValue();
    }

    public Vector3i getVector3iProperty(EntityMetaPropertyName property) {
        EntityMetaProperty<Vector3i> storedProperty = (EntityMetaProperty<Vector3i>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.VECTOR3I) {
            throw new IllegalArgumentException(property + " was not a Vector3i property.");
        }
        return storedProperty.getValue();
    }

    public Vector3 getVector3Property(EntityMetaPropertyName property) {
        EntityMetaProperty<Vector3> storedProperty = (EntityMetaProperty<Vector3>)this.properties.get(property);
        if (storedProperty.getType() != EntityMetaPropertyType.VECTOR3) {
            throw new IllegalArgumentException(property + " was not a Vector3 property.");
        }
        return storedProperty.getValue();
    }


}
