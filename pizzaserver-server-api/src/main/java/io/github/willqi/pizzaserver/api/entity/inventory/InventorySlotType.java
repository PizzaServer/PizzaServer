package io.github.willqi.pizzaserver.api.entity.inventory;

/**
 * All the possible inventory slot types an inventory can have.
 */
public enum InventorySlotType {
    ANVIL_ITEM(0),
    ANVIL_MATERIAL(1),
    ANVIL_RESULT(2),
    SMITHING_TABLE_ITEM(0),
    SMITHING_TABLE_MATERIAL(1),
    SMITHING_TABLE_RESULT(2),
    ARMOR(0, 3),
    CONTAINER(0, 26),
    BEACON_ITEM(0),
    BREWING_ITEM(0),
    BREWING_RESULT(1),
    BREWING_FUEL(0),
    PLAYER_INVENTORY(0, 35), // TODO: when is this used
    CRAFTING_ITEM(0, 3),
    CRAFTING_RESULT(0),
    RECIPE_CONSTRUCTION(0),
    RECIPE_NATURE(0),
    RECIPE_ITEMS(0),
    RECIPE_SEARCH(0),
    RECIPE_SEARCH_BAR(0),
    RECIPE_EQUIPMENT(0),
    ENCHANTING_ITEM(0),
    ENCHANTING_FUEL(1),
    FURNACE_FUEL(1),
    FURNACE_ITEM(0),
    FURNACE_RESULT(2),
    HORSE_INVENTORY(0, 1), // TODO: double check
    HOTBAR(0, 8),
    INVENTORY(0, 35),
    SHULKER(0, 26),
    TRADE_ITEM(0),
    TRADE_ITEM_2(1),
    TRADE_RESULT(2),
    OFFHAND(1),
    COMPCREATE_ITEM(0),    // ???
    COMPCREATE_RESULT(1),  // ???
    ELEMCONSTRUCT_ITEM(0), // ???
    MATREDUCE_ITEM(0),     // ???
    MATREDUCE_OUTPUT(1),   // ???
    LABTABLE_ITEM(0),
    LOOM_ITEM(0),
    LOOM_DYE(1),
    LOOM_MATERIAL(2),
    LOOM_RESULT(3),
    BLAST_FURNACE_ITEM(0),
    SMOKER_ITEM(0),
    TRADE2_INGREDIENT(4),
    TRADE2_INGREDIENT2(-1),  // Unknown. When is this used?
    TRADE2_RESULT(-1),          // Unknown. When is this used?
    GRINDSTONE_ITEM(0),
    GRINDSTONE_MATERIAL(1),
    GRINDSTONE_RESULT(2),
    STONECUTTER_ITEM(0),
    STONECUTTER_RESULT(1),
    CARTOGRAPHY_ITEM(0),
    CARTOGRAPHY_MATERIAL(1),
    CARTOGRAPHY_RESULT(2),
    BARREL(0, 26),
    CURSOR(0),
    CREATIVE_RESULT(50);


    private final int minSlotId;
    private final int maxSlotId;

    InventorySlotType(int slotId) {
        this(slotId, slotId);
    }

    InventorySlotType(int minSlotId, int maxSlotId) {
        this.minSlotId = minSlotId;
        this.maxSlotId = maxSlotId;
    }

    /**
     * Check if the slot provided is a slot this slot type supports.
     * @param slot slot number
     * @return if the slot number is valid for this type
     */
    public boolean isValidSlot(int slot) {
        return this.maxSlotId >= slot && slot >= this.minSlotId;
    }

}
