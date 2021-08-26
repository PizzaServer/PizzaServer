package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.data.NetworkItemStackData;

/**
 * Sent by the server to update the armor of an entity
 */
public class MobArmourEquipmentPacket extends BaseBedrockPacket {

    public static final int ID = 0x20;

    private long entityRuntimeId;

    private NetworkItemStackData helmet;
    private NetworkItemStackData chestplate;
    private NetworkItemStackData leggings;
    private NetworkItemStackData boots;


    public MobArmourEquipmentPacket() {
        super(ID);
    }

    public long getEntityRuntimeId() {
        return this.entityRuntimeId;
    }

    public void setEntityRuntimeId(long entityRuntimeId) {
        this.entityRuntimeId = entityRuntimeId;
    }

    public NetworkItemStackData getHelmetNetworkData() {
        return this.helmet;
    }

    public void setHelmetNetworkData(NetworkItemStackData helmetNetworkData) {
        this.helmet = helmetNetworkData;
    }

    public NetworkItemStackData getChestplateNetworkData() {
        return this.chestplate;
    }

    public void setChestplateNetworkData(NetworkItemStackData chestplateNetworkData) {
        this.chestplate = chestplateNetworkData;
    }

    public NetworkItemStackData getLeggingsNetworkData() {
        return this.leggings;
    }

    public void setLeggingsNetworkData(NetworkItemStackData leggingsNetworkData) {
        this.leggings = leggingsNetworkData;
    }

    public NetworkItemStackData getBootsNetworkData() {
        return this.boots;
    }

    public void setBootsNetworkData(NetworkItemStackData bootsNetworkData) {
        this.boots = bootsNetworkData;
    }

}
