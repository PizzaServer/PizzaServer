package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.InventoryType;

public class ContainerOpenPacket extends BaseBedrockPacket {

    public static final int ID = 0x2e;

    private int inventoryId;
    private InventoryType inventoryType;
    private Vector3i coordinates;
    private long entityRuntimeId;


    public ContainerOpenPacket() {
        super(ID);
    }

    public int getInventoryId() {
        return this.inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public InventoryType getInventoryType() {
        return this.inventoryType;
    }

    public void setInventoryType(InventoryType type) {
        this.inventoryType = type;
    }

    public Vector3i getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(Vector3i coordinates) {
        this.coordinates = coordinates;
    }

    public long getEntityRuntimeId() {
        return this.entityRuntimeId;
    }

    public void setEntityRuntimeId(long entityRuntimeId) {
        this.entityRuntimeId = entityRuntimeId;
    }

}
