package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.packs.ResourcePack;

import java.util.HashSet;
import java.util.Set;

/**
 * Sent to inform the client of all packs the server has.
 */
public class ResourcePacksInfoPacket extends BaseBedrockPacket {

    public static final int ID = 0x06;

    private boolean forcedToAccept;
    private boolean forcingServerPacks;
    private boolean scriptingEnabled;
    private Set<ResourcePack> resourcePacks = new HashSet<>();
    private Set<ResourcePack> behaviorPacks = new HashSet<>();

    public ResourcePacksInfoPacket() {
        super(ID);
    }

    public boolean isForcedToAccept() {
        return this.forcedToAccept;
    }

    public void setForcedToAccept(boolean forced) {
        this.forcedToAccept = forced;
    }

    public boolean isForcingServerPacks() {
        return this.forcingServerPacks;
    }

    public void setForcingServerPacks(boolean forced) {
        this.forcingServerPacks = forced;
    }

    public boolean isScriptingEnabled() {
        return this.scriptingEnabled;
    }

    public void setScriptingEnabled(boolean enabled) {
        this.scriptingEnabled = enabled;
    }

    public Set<ResourcePack> getBehaviorPacks() {
        return this.behaviorPacks;
    }

    public void setBehaviorPacks(Set<ResourcePack> behaviorPacks) {
        this.behaviorPacks = behaviorPacks;
    }

    public Set<ResourcePack> getResourcePacks() {
        return this.resourcePacks;
    }

    public void setResourcePacks(Set<ResourcePack> resourcePacks) {
        this.resourcePacks = resourcePacks;
    }

}
