package io.github.willqi.pizzaserver.api.network.protocol.packets;

public class ContainerClosePacket extends BaseBedrockPacket {

    public static final int ID = 0x2f;

    private int inventoryId;
    private boolean closedByServer;


    public ContainerClosePacket() {
        super(ID);
    }

    public int getInventoryId() {
        return this.inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public boolean isClosedByServer() {
        return this.closedByServer;
    }

    public void setClosedByServer(boolean closedByServer) {
        this.closedByServer = closedByServer;
    }

}
