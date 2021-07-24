package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.network.protocol.data.Experiment;
import io.github.willqi.pizzaserver.api.packs.APIDataPack;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Sent after the client responds with a HAVE_ALL_PACKS from the ResourcePackResponsePacket.
 * Contains all of the packs the server supports
 */
public class ResourcePackStackPacket extends BedrockPacket {

    public static final int ID = 0x07;

    private boolean forcedToAccept;
    private Set<APIDataPack> resourcePacks = new HashSet<>();
    private Set<APIDataPack> behaviourPacks = new HashSet<>();

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

    public Set<APIDataPack> getResourcePacks() {
        return this.resourcePacks;
    }

    public void setResourcePacks(Set<APIDataPack> resourcePacks) {
        this.resourcePacks = resourcePacks;
    }

    public Set<APIDataPack> getBehaviourPacks() {
        return this.behaviourPacks;
    }

    public void setBehaviourPacks(Set<APIDataPack> behaviourPacks) {
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
