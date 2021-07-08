package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.packs.DataPack;

import java.util.Collection;
import java.util.HashSet;

/**
 * Sent to inform the client of all packs the server has
 */
public class ResourcePacksInfoPacket extends BedrockPacket {

    public static final int ID = 0x06;

    private boolean forcedToAccept;
    private boolean scriptingEnabled;
    private Collection<DataPack> resourcePacks = new HashSet<>();
    private Collection<DataPack> behaviorPacks = new HashSet<>();

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

    public Collection<DataPack> getBehaviorPacks() {
        return this.behaviorPacks;
    }

    public void setBehaviorPacks(Collection<DataPack> behaviorPacks) {
        this.behaviorPacks = behaviorPacks;
    }

    public Collection<DataPack> getResourcePacks() {
        return this.resourcePacks;
    }

    public void setResourcePacks(Collection<DataPack> resourcePacks) {
        this.resourcePacks = resourcePacks;
    }

}
