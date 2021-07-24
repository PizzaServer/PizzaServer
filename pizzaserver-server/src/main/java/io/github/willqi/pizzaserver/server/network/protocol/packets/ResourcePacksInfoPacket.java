package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.packs.APIDataPack;

import java.util.HashSet;
import java.util.Set;

/**
 * Sent to inform the client of all packs the server has
 */
public class ResourcePacksInfoPacket extends BedrockPacket {

    public static final int ID = 0x06;

    private boolean forcedToAccept;
    private boolean scriptingEnabled;
    private Set<APIDataPack> resourcePacks = new HashSet<>();
    private Set<APIDataPack> behaviorPacks = new HashSet<>();

    public ResourcePacksInfoPacket() {
        super(ID);
    }

    public boolean isForcedToAccept() {
        return this.forcedToAccept;
    }

    public void setForcedToAccept(boolean forced) {
        this.forcedToAccept = forced;
    }

    public boolean isScriptingEnabled() {
        return this.scriptingEnabled;
    }

    public void setScriptingEnabled(boolean enabled) {
        this.scriptingEnabled = enabled;
    }

    public Set<APIDataPack> getBehaviorPacks() {
        return this.behaviorPacks;
    }

    public void setBehaviorPacks(Set<APIDataPack> behaviorPacks) {
        this.behaviorPacks = behaviorPacks;
    }

    public Set<APIDataPack> getResourcePacks() {
        return this.resourcePacks;
    }

    public void setResourcePacks(Set<APIDataPack> resourcePacks) {
        this.resourcePacks = resourcePacks;
    }

}
