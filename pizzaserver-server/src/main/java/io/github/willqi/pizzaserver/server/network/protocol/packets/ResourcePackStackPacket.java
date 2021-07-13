package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.packs.DataPack;

import java.util.HashSet;
import java.util.Set;

/**
 * Sent after the client responds with a HAVE_ALL_PACKS from the ResourcePackResponsePacket.
 * Contains all of the packs the server supports
 */
public class ResourcePackStackPacket extends BedrockPacket {

    public static final int ID = 0x07;

    private boolean forcedToAccept;
    private Set<DataPack> resourcePacks = new HashSet<>();
    private Set<DataPack> behaviourPacks = new HashSet<>();

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

    public Set<DataPack> getResourcePacks() {
        return this.resourcePacks;
    }

    public void setResourcePacks(Set<DataPack> resourcePacks) {
        this.resourcePacks = resourcePacks;
    }

    public Set<DataPack> getBehaviourPacks() {
        return this.behaviourPacks;
    }

    public void setBehaviourPacks(Set<DataPack> behaviourPacks) {
        this.behaviourPacks = behaviourPacks;
    }

    public String getGameVersion() {
        return this.gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }


}
