package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.packs.DataPack;

public class ResourcePacksInfoPacket extends BedrockPacket {

    public static final int ID = 0x06;

    private boolean forcedToAccept;
    private boolean scriptingEnabled;
    private DataPack[] resourcePacks = new DataPack[0];
    private DataPack[] behaviorPacks = new DataPack[0];

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

    public DataPack[] getBehaviorPacks() {
        return this.behaviorPacks;
    }

    public void setBehaviorPacks(DataPack[] behaviorPacks) {
        this.behaviorPacks = behaviorPacks;
    }

    public DataPack[] getResourcePacks() {
        return this.resourcePacks;
    }

    public void setResourcePacks(DataPack[] resourcePacks) {
        this.resourcePacks = resourcePacks;
    }

}
