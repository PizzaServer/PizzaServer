package io.github.willqi.pizzaserver.api.item;

public enum ItemToolType {
    ANY,

    SHEARS(ANY),

    WOOD_SWORD(ANY),
    WOOD_AXE(ANY),
    WOOD_PICKAXE(ANY),
    WOOD_HOE(ANY),
    WOOD_SHOVEL(ANY),

    STONE_SWORD(WOOD_SWORD),
    STONE_AXE(WOOD_AXE),
    STONE_PICKAXE(WOOD_PICKAXE),
    STONE_HOE(WOOD_HOE),
    STONE_SHOVEL(WOOD_SHOVEL),

    IRON_SWORD(STONE_SWORD),
    IRON_AXE(STONE_AXE),
    IRON_PICKAXE(STONE_PICKAXE),
    IRON_HOE(STONE_HOE),
    IRON_SHOVEL(STONE_SHOVEL),

    GOLD_SWORD(IRON_SWORD),
    GOLD_AXE(IRON_AXE),
    GOLD_PICKAXE(IRON_PICKAXE),
    GOLD_HOE(IRON_HOE),
    GOLD_SHOVEL(IRON_SHOVEL),

    DIAMOND_SWORD(GOLD_SWORD),
    DIAMOND_AXE(GOLD_AXE),
    DIAMOND_PICKAXE(GOLD_PICKAXE),
    DIAMOND_HOE(GOLD_HOE),
    DIAMOND_SHOVEL(GOLD_SHOVEL),

    NETHERITE_SWORD(DIAMOND_SWORD),
    NETHERITE_AXE(DIAMOND_AXE),
    NETHERITE_PICKAXE(DIAMOND_PICKAXE),
    NETHERITE_HOE(DIAMOND_HOE),
    NETHERITE_SHOVEL(DIAMOND_SHOVEL);


    private final ItemToolType predecessor;


    ItemToolType() {
        this.predecessor = null;
    }

    ItemToolType(ItemToolType predecessor) {
        this.predecessor = predecessor;
    }

    public ItemToolType getPredecessor() {
        return this.predecessor;
    }

}
