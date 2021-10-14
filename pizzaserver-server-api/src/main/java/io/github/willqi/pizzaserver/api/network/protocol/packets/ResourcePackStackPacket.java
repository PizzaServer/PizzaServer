package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.data.Experiment;
import io.github.willqi.pizzaserver.api.packs.ResourcePack;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Sent after the client responds with a HAVE_ALL_PACKS from the ResourcePackResponsePacket.
 * Contains all the packs the server supports
 */
public class ResourcePackStackPacket extends BaseBedrockPacket {

    public static final int ID = 0x07;

    private boolean forcedToAccept;
    private Set<ResourcePack> resourcePacks = new HashSet<>();
    private Set<ResourcePack> behaviourPacks = new HashSet<>();

    private Set<Experiment> experiments = new HashSet<>();
    private boolean experimentsPreviouslyEnabled;

    private String gameVersion;


    public ResourcePackStackPacket() {
        super(ID);
    }

    public boolean isForcedToAccept() {
        return this.forcedToAccept;
    }

    public void setForcedToAccept(boolean forced) {
        this.forcedToAccept = forced;
    }

    public Set<ResourcePack> getResourcePacks() {
        return this.resourcePacks;
    }

    public void setResourcePacks(Set<ResourcePack> resourcePacks) {
        this.resourcePacks = resourcePacks;
    }

    public Set<ResourcePack> getBehaviourPacks() {
        return this.behaviourPacks;
    }

    public void setBehaviourPacks(Set<ResourcePack> behaviourPacks) {
        this.behaviourPacks = behaviourPacks;
    }

    public Set<Experiment> getExperiments() {
        return Collections.unmodifiableSet(this.experiments);
    }

    public void setExperiments(Set<Experiment> experiments) {
        this.experiments = experiments;
    }

    public boolean isExperimentsPreviouslyEnabled() {
        return this.experimentsPreviouslyEnabled;
    }

    public void setExperimentsPreviouslyEnabled(boolean previouslyEnabled) {
        this.experimentsPreviouslyEnabled = previouslyEnabled;
    }

    public String getGameVersion() {
        return this.gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }


}
