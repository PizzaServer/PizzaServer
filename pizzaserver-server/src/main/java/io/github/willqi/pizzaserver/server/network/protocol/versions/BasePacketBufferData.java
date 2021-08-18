package io.github.willqi.pizzaserver.server.network.protocol.versions;

import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlagCategory;
import io.github.willqi.pizzaserver.api.entity.meta.properties.EntityMetaPropertyName;
import io.github.willqi.pizzaserver.server.network.protocol.data.Experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Stores version specific data that PacketBuffers use
 */
public abstract class BasePacketBufferData {

    private final Set<Experiment> supportedExperiments = new HashSet<>();

    private final Map<EntityMetaFlagCategory, Integer> supportedEntityFlagCategories = new HashMap<>();
    private final Map<EntityMetaFlag, Integer> supportedEntityFlags = new HashMap<>();
    private final Map<EntityMetaPropertyName, Integer> supportedEntityProperties = new HashMap<>();


    //
    // Experiments
    //

    public BasePacketBufferData registerExperiment(Experiment experiment) {
        this.supportedExperiments.add(experiment);
        return this;
    }

    public boolean isExperimentSupported(Experiment experiment) {
        return this.supportedExperiments.contains(experiment);
    }


    //
    // Entity Metadata
    //

    public BasePacketBufferData registerEntityFlagCategory(EntityMetaFlagCategory category, int value) {
        this.supportedEntityFlagCategories.put(category, value);
        return this;
    }

    public boolean isEntityFlagCategorySupported(EntityMetaFlagCategory category) {
        return this.supportedEntityFlagCategories.containsKey(category);
    }

    public int getEntityMetaFlagCategoryId(EntityMetaFlagCategory category) {
        return this.supportedEntityFlagCategories.get(category);
    }


    public BasePacketBufferData registerEntityFlag(EntityMetaFlag flag, int value) {
        this.supportedEntityFlags.put(flag, value);
        return this;
    }

    public boolean isEntityFlagSupported(EntityMetaFlag flag) {
        return this.supportedEntityFlags.containsKey(flag);
    }

    public int getEntityFlagId(EntityMetaFlag flag) {
        return this.supportedEntityFlags.get(flag);
    }

    public BasePacketBufferData registerEntityProperty(EntityMetaPropertyName propertyName, int value) {
        this.supportedEntityProperties.put(propertyName, value);
        return this;
    }

    public boolean isEntityPropertySupported(EntityMetaPropertyName propertyName) {
        return this.supportedEntityProperties.containsKey(propertyName);
    }

    public int getEntityPropertyId(EntityMetaPropertyName propertyName) {
        return this.supportedEntityProperties.get(propertyName);
    }



}
