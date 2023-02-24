package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockFurnace;
import io.github.pizzaserver.api.inventory.FurnaceInventory;

public interface BlockEntityFurnace extends BlockEntityContainer<BlockFurnace> {

    String ID = "Furnace";

    @Override
    default String getId() {
        return ID;
    }

    FurnaceInventory getInventory();

    int getFuelTicks();

    void setFuelTicks(int ticks);

    int getFuelDurationTicks();

    void setFuelDurationTicks(int ticks);

    /**
     * Get the amount of ticks since the beginning of the ingredient's cooking.
     * @return ticks
     */
    int getCookTimeTicks();

    /**
     * Set the amount of ticks since the beginning of the ingredient's cooking.
     * @param ticks ticks
     */
    void setCookTimeTicks(int ticks);

}
