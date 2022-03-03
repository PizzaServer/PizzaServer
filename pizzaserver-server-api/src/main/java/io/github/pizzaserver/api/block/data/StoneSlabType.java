package io.github.pizzaserver.api.block.data;

public enum StoneSlabType {
    SMOOTH_STONE("Smooth Stone Slab"),
    SANDSTONE("Sandstone Slab"),
    WOOD("Wooden Slab"),
    COBBLESTONE("Cobblestone Slab"),
    BRICK("Brick Slab"),
    STONE_BRICK("Stone Brick Slab"),
    QUARTZ("Quartz Slab"),
    NETHER_BRICK("Nether Brick Slab"),
    RED_SANDSTONE("Red Sandstone Slab"),
    PURPUR("Purpur Slab"),
    PRISMARINE_ROUGH("Rough Prismarine Slab"),
    PRISMARINE_DARK("Dark Prismarine Slab"),
    PRISMARINE_BRICK("Prismarine Brick Slab"),
    MOSSY_COBBLESTONE("Mossy Cobblestone Slab"),
    SMOOTH_SANDSTONE("Smooth Sandstone Slab"),
    RED_NETHER_BRICK("Red Nether Brick Slab"),
    END_STONE_BRICK("Endstone Brick Slab"),
    SMOOTH_RED_SANDSTONE("Smooth Red Sandstone Slab"),
    POLISHED_ANDESITE("Polished Andesite Slab"),
    ANDESITE("Andesite Slab"),
    DIORITE("Diorite Slab"),
    POLISHED_DIORITE("Polished Diorite Slab"),
    GRANITE("Granite Slab"),
    POLISHED_GRANITE("Polished Granite Slab"),
    MOSSY_STONE_BRICK("Mossy Stone Brick Slab"),
    SMOOTH_QUARTZ("Smooth Quartz Slab"),
    STONE("Stone Slab"),
    CUT_SANDSTONE("Cut Sandstone Slab"),
    CUT_RED_SANDSTONE("Cut Red Sandstone Slab");


    private final String displayName;


    StoneSlabType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
