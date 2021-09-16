package io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions;

public enum InventoryActionType {

    /**
     * When a player picks a stack
     */
    TAKE,

    /**
     * When a player places down a stack
     */
    PLACE,

    /**
     * When a player wants to swap a stack with another stack
     */
    SWAP,

    /**
     * When a player drags a stack out of their inventory
     */
    DROP,

    /**
     * When a player drags a stack into the creative menu
     */
    DESTROY,

    /**
     * When a player crafts a recipe this is called for the ingredients
     */
    CONSUME,

    /**
     * When a player crafts a recipe and creates byproducts
     */
    CREATE,

    LAB_TABLE_COMBINE,

    /**
     * When a player confirms the beacon enhancements they want
     */
    BEACON_PAYMENT,

    /**
     * When a player breaks a block
     * Used in v428 and higher
     */
    MINE_BLOCK,

    /**
     * When a player starts crafting a recipe
     */
    CRAFT_RECIPE,

    /**
     * When a player autocrafts a recipe
     */
    AUTO_CRAFT_RECIPE,

    /**
     * When a player retrieves an item from the creative menu
     */
    CRAFT_CREATIVE,

    /**
     * Used in v422 and higher
     */
    CRAFT_RECIPE_OPTIONAL,

    /**
     * When a player does an unimplemented inventory action
     */
    CRAFT_NOT_IMPLEMENTED,

    /**
     * When a player finishes crafting a recipe
     */
    CRAFT_RESULTS_DEPRECATED
}
