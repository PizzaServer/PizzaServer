package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.packs.DataPack;

public class ResourcePackStackPacket extends BedrockPacket {

    public static final int ID = 0x07;

    private boolean forcedToAccept;
    private DataPack[] resourcePacks = new DataPack[0];
    private DataPack[] behaviourPacks = new DataPack[0];

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

    public DataPack[] getResourcePacks() {
        return this.resourcePacks;
    }

    public void setResourcePacks(DataPack[] resourcePacks) {
        this.resourcePacks = resourcePacks;
    }

    public DataPack[] getBehaviourPacks() {
        return this.behaviourPacks;
    }

    public void setBehaviourPacks(DataPack[] behaviourPacks) {
        this.behaviourPacks = behaviourPacks;
    }

    public String getGameVersion() {
        return this.gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }


}
