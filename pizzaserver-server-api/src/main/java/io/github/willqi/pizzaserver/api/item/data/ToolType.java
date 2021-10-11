package io.github.willqi.pizzaserver.api.item.data;

public enum ToolType {

    NONE(1),

    SHEARS(NONE, 1),

    WOOD_SWORD(NONE, 1.5f),
    WOOD_AXE(NONE, 2),
    WOOD_PICKAXE(NONE, 2),
    WOOD_HOE(NONE, 2),
    WOOD_SHOVEL(NONE, 2),

    STONE_SWORD(WOOD_SWORD, 1.5f),
    STONE_AXE(WOOD_AXE, 4),
    STONE_PICKAXE(WOOD_PICKAXE, 4),
    STONE_HOE(WOOD_HOE, 4),
    STONE_SHOVEL(WOOD_SHOVEL, 4),

    IRON_SWORD(STONE_SWORD, 1.5f),
    IRON_AXE(STONE_AXE, 6),
    IRON_PICKAXE(STONE_PICKAXE, 6),
    IRON_HOE(STONE_HOE, 6),
    IRON_SHOVEL(STONE_SHOVEL, 6),

    GOLD_SWORD(IRON_SWORD, 1.5f),
    GOLD_AXE(IRON_AXE, 12),
    GOLD_PICKAXE(IRON_PICKAXE, 12),
    GOLD_HOE(IRON_HOE, 12),
    GOLD_SHOVEL(IRON_SHOVEL, 12),

    DIAMOND_SWORD(GOLD_SWORD, 1.5f),
    DIAMOND_AXE(GOLD_AXE, 8),
    DIAMOND_PICKAXE(GOLD_PICKAXE, 8),
    DIAMOND_HOE(GOLD_HOE, 8),
    DIAMOND_SHOVEL(GOLD_SHOVEL, 8),

    NETHERITE_SWORD(DIAMOND_SWORD, 1.5f),
    NETHERITE_AXE(DIAMOND_AXE, 9),
    NETHERITE_PICKAXE(DIAMOND_PICKAXE, 9),
    NETHERITE_HOE(DIAMOND_HOE, 9),
    NETHERITE_SHOVEL(DIAMOND_SHOVEL, 9);


    private final ToolType predecessor;
    private final float strength;


    ToolType(float strength) {
        this(null, strength);
    }

    ToolType(ToolType predecessor, float strength) {
        this.predecessor = predecessor;
        this.strength = strength;
    }

    public ToolType getPredecessor() {
        return this.predecessor;
    }

    public float getStrength() {
        return this.strength;
    }

}
