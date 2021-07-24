package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.packs.DataPack;

import java.util.HashSet;
import java.util.Set;

/**
 * Sent to inform the client of all packs the server has
 */
public class ResourcePacksInfoPacket extends ImplBedrockPacket {

    public static final int ID = 0x06;

    private boolean forcedToAccept;
    private boolean scriptingEnabled;
    private Set<DataPack> resourcePacks = new HashSet<>();
    private Set<DataPack> behaviorPacks = new HashSet<>();

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

    public Set<DataPack> getBehaviorPacks() {
        return this.behaviorPacks;
    }

    public void setBehaviorPacks(Set<DataPack> behaviorPacks) {
        this.behaviorPacks = behaviorPacks;
    }

    public Set<DataPack> getResourcePacks() {
        return this.resourcePacks;
    }

    public void setResourcePacks(Set<DataPack> resourcePacks) {
        this.resourcePacks = resourcePacks;
    }

}
