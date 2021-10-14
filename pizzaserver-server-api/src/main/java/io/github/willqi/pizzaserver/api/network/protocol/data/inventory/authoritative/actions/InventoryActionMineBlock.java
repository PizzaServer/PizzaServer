package io.github.willqi.pizzaserver.api.network.protocol.data.inventory.authoritative.actions;

/**
 * Used for server authoritative inventory.
 * Received when a player breaks a block
 */
public class InventoryActionMineBlock implements InventoryAction {

    private final int unknown;
    private final int networkStackId;
    private final int predictedDurability;


    public InventoryActionMineBlock(int unknown, int predictedDurability, int networkStackId) {
        this.unknown = unknown;
        this.predictedDurability = predictedDurability;
        this.networkStackId = networkStackId;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.MINE_BLOCK;
    }

    public int getNetworkStackId() {
        return this.networkStackId;
    }

    public int getPredictedDurability() {
        return this.predictedDurability;
    }

}
