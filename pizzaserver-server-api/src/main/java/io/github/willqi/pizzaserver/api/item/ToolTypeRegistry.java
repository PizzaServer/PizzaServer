package io.github.willqi.pizzaserver.api.item;

import io.github.willqi.pizzaserver.api.item.data.ToolType;
import io.github.willqi.pizzaserver.api.item.data.ToolTypeID;

import java.util.HashMap;
import java.util.Map;

public class ToolTypeRegistry {

    private static final Map<String, ToolType> types = new HashMap<String, ToolType>() {
        {
            this.put(ToolTypeID.NONE, new ToolType(null, null, 1));

            this.put(ToolTypeID.SHEARS, new ToolType(null, null, 1));

            this.put(ToolTypeID.WOOD_SWORD, new ToolType(ToolTypeID.STONE_SWORD, ToolTypeID.NONE, 1.5f));
            this.put(ToolTypeID.WOOD_AXE, new ToolType(ToolTypeID.STONE_AXE, ToolTypeID.NONE, 2));
            this.put(ToolTypeID.WOOD_PICKAXE, new ToolType(ToolTypeID.STONE_PICKAXE, ToolTypeID.NONE, 2));
            this.put(ToolTypeID.WOOD_HOE, new ToolType(ToolTypeID.STONE_HOE, ToolTypeID.NONE, 2));
            this.put(ToolTypeID.WOOD_SHOVEL, new ToolType(ToolTypeID.STONE_SHOVEL, ToolTypeID.NONE, 2));

            this.put(ToolTypeID.STONE_SWORD, new ToolType(ToolTypeID.IRON_SWORD, ToolTypeID.WOOD_SWORD, 1.5f));
            this.put(ToolTypeID.STONE_AXE, new ToolType(ToolTypeID.IRON_AXE, ToolTypeID.WOOD_AXE, 4));
            this.put(ToolTypeID.STONE_PICKAXE, new ToolType(ToolTypeID.IRON_PICKAXE, ToolTypeID.WOOD_PICKAXE, 4));
            this.put(ToolTypeID.STONE_HOE, new ToolType(ToolTypeID.IRON_HOE, ToolTypeID.WOOD_HOE, 4));
            this.put(ToolTypeID.STONE_SHOVEL, new ToolType(ToolTypeID.IRON_SHOVEL, ToolTypeID.WOOD_SHOVEL, 4));

            this.put(ToolTypeID.IRON_SWORD, new ToolType(ToolTypeID.GOLD_SWORD, ToolTypeID.STONE_SWORD, 1.5f));
            this.put(ToolTypeID.IRON_AXE, new ToolType(ToolTypeID.GOLD_AXE, ToolTypeID.STONE_AXE, 6));
            this.put(ToolTypeID.IRON_PICKAXE, new ToolType(ToolTypeID.GOLD_PICKAXE, ToolTypeID.STONE_PICKAXE, 6));
            this.put(ToolTypeID.IRON_HOE, new ToolType(ToolTypeID.GOLD_HOE, ToolTypeID.STONE_HOE, 6));
            this.put(ToolTypeID.IRON_SHOVEL, new ToolType(ToolTypeID.GOLD_SHOVEL, ToolTypeID.STONE_SHOVEL, 6));

            this.put(ToolTypeID.GOLD_SWORD, new ToolType(ToolTypeID.DIAMOND_SWORD, ToolTypeID.IRON_SWORD, 1.5f));
            this.put(ToolTypeID.GOLD_AXE, new ToolType(ToolTypeID.DIAMOND_AXE, ToolTypeID.IRON_AXE, 12));
            this.put(ToolTypeID.GOLD_PICKAXE, new ToolType(ToolTypeID.DIAMOND_PICKAXE, ToolTypeID.IRON_PICKAXE, 12));
            this.put(ToolTypeID.GOLD_HOE, new ToolType(ToolTypeID.DIAMOND_HOE, ToolTypeID.IRON_HOE, 12));
            this.put(ToolTypeID.GOLD_SHOVEL, new ToolType(ToolTypeID.DIAMOND_SHOVEL, ToolTypeID.IRON_SHOVEL, 12));

            this.put(ToolTypeID.DIAMOND_SWORD, new ToolType(ToolTypeID.NETHERITE_SWORD, ToolTypeID.GOLD_SWORD, 1.5f));
            this.put(ToolTypeID.DIAMOND_AXE, new ToolType(ToolTypeID.NETHERITE_AXE, ToolTypeID.GOLD_AXE, 8));
            this.put(ToolTypeID.DIAMOND_PICKAXE, new ToolType(ToolTypeID.NETHERITE_PICKAXE, ToolTypeID.GOLD_PICKAXE, 8));
            this.put(ToolTypeID.DIAMOND_HOE, new ToolType(ToolTypeID.NETHERITE_HOE, ToolTypeID.GOLD_HOE, 8));
            this.put(ToolTypeID.DIAMOND_SHOVEL, new ToolType(ToolTypeID.NETHERITE_SHOVEL, ToolTypeID.GOLD_SHOVEL, 8));

            this.put(ToolTypeID.NETHERITE_SWORD, new ToolType(null, ToolTypeID.DIAMOND_SWORD, 1.5f));
            this.put(ToolTypeID.NETHERITE_AXE, new ToolType(null, ToolTypeID.DIAMOND_AXE, 9));
            this.put(ToolTypeID.NETHERITE_PICKAXE, new ToolType(null, ToolTypeID.DIAMOND_PICKAXE, 9));
            this.put(ToolTypeID.NETHERITE_HOE, new ToolType(null, ToolTypeID.DIAMOND_HOE, 9));
            this.put(ToolTypeID.NETHERITE_SHOVEL, new ToolType(null, ToolTypeID.DIAMOND_SHOVEL, 9));
        }
    };


    public static ToolType getToolType(String id) {
        return types.get(id);
    }

    public static void register(String id, ToolType toolType) {
        types.put(id, toolType);
    }

}
