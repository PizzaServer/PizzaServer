package io.github.willqi.pizzaserver.server.network.protocol.data.inventory;

public enum InventoryType {
    INVENTORY,
    CHEST,
    CRAFTING_TABLE,
    FURNACE,
    ENCHANTMENT_TABLE,
    BREWING_STAND,
    ANVIL,
    DISPENSER,
    DROPPER,
    HOPPER,
    CAULDRON,
    MINECART_CHEST,
    MINECART_HOPPER,
    HORSE,
    BEACON,
    STRUCTURE_MENU,
    TRADE_MENU,
    COMMAND_BLOCK,
    JUKEBOX,
    ARMOR,
    HAND,
    COMPOUND_CREATOR,       // prob EDU
    ELEMENT_CONSTRUCTOR,    // prob EDU
    MATERIAL_REDUCER,
    LAB_TABLE,
    LOOM,
    LECTERN,
    GRINDSTONE,
    BLAST_FURNACE,
    SMOKER,
    STONECUTTER,
    CARTOGRAPHY,
    HUD,
    JIGSAW_MENU,
    SMITHING_TABLE;


    public int getId() {
        return this.ordinal() - 1;  // starts at -1
    }

}
