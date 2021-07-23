package io.github.willqi.pizzaserver.api.entity.meta;

import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlagCategory;
import io.github.willqi.pizzaserver.api.entity.meta.properties.APIEntityMetaProperty;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.util.Map;
import java.util.Set;

/**
 * Entities have metadata which gives them special properties about how they are rendered to the Minecraft client
 */
public interface APIEntityMetaData {

    /**
     * Retrieve all enabled entity flags
     * @return all enabled entity flags
     */
    Map<EntityMetaFlagCategory, Set<EntityMetaFlag>> getFlags();

    /**
     * Check if the metadata has {@link EntityMetaFlag} enabled under the {@link EntityMetaFlagCategory}
     * @param flagCategory the category the flag falls under
     * @param flag the flag to check
     * @return if the flag is enabled
     */
    boolean hasFlag(EntityMetaFlagCategory flagCategory, EntityMetaFlag flag);

    /**
     * Change the status of a {@link EntityMetaFlag} under a {@link EntityMetaFlagCategory}
     * @param flagCategory the category the flag falls under
     * @param flag the flag to check
     * @param enabled if the flag should be enabled
     */
    void setFlag(EntityMetaFlagCategory flagCategory, EntityMetaFlag flag, boolean enabled);

    /**
     * Get all of entity data properties set
     * @return a map of all properties
     */
    Map<EntityMetaPropertyName, APIEntityMetaProperty<?>> getProperties();

    /**
     * Check if the metadata has a property set
     * @param propertyName the {@link EntityMetaPropertyName} we are checking
     * @return if it is set
     */
    boolean hasProperty(EntityMetaPropertyName propertyName);

    byte getByteProperty(EntityMetaPropertyName propertyName);

    void setByteProperty(EntityMetaPropertyName propertyName, byte value);

    short getShortProperty(EntityMetaPropertyName propertyName);

    void setShortProperty(EntityMetaPropertyName propertyName, short value);

    int getIntProperty(EntityMetaPropertyName propertyName);

    void setIntProperty(EntityMetaPropertyName propertyName, int value);

    float getFloatProperty(EntityMetaPropertyName propertyName);

    void setFloatProperty(EntityMetaPropertyName propertyName, float value);

    long getLongProperty(EntityMetaPropertyName propertyName);

    void setLongProperty(EntityMetaPropertyName propertyName, long value);

    String getStringProperty(EntityMetaPropertyName propertyName);

    void setStringProperty(EntityMetaPropertyName propertyName, String value);

    NBTCompound getNBTProperty(EntityMetaPropertyName propertyName);

    void setNBTProperty(EntityMetaPropertyName propertyName, NBTCompound value);

    Vector3i getVector3iProperty(EntityMetaPropertyName propertyName);

    void setVector3iProperty(EntityMetaPropertyName propertyName, Vector3i value);

    Vector3 getVector3Property(EntityMetaPropertyName propertyName);

    void setVector3Property(EntityMetaPropertyName propertyName, Vector3 value);

}
