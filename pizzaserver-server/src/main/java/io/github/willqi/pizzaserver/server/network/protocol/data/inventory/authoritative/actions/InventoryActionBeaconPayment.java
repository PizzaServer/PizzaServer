package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions;

public class InventoryActionBeaconPayment implements InventoryAction {

    private final int selectedPrimaryEffect;
    private final int selectedSecondaryEffect;


    public InventoryActionBeaconPayment(int selectedPrimaryEffect, int selectedSecondaryEffect) {
        this.selectedPrimaryEffect = selectedPrimaryEffect;
        this.selectedSecondaryEffect = selectedSecondaryEffect;
    }

    @Override
    public InventoryActionType getType() {
        return InventoryActionType.BEACON_PAYMENT;
    }

    public int getSelectedPrimaryEffect() {
        return this.selectedPrimaryEffect;
    }

    public int getSelectedSecondaryEffect() {
        return this.selectedSecondaryEffect;
    }

}
