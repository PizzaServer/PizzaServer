package io.github.willqi.pizzaserver.server;

import io.github.willqi.pizzaserver.api.entity.EntityRegistry;
import io.github.willqi.pizzaserver.api.entity.types.impl.*;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.server.level.world.blocks.types.*;

public class VanillaContentLoader {

    public static void load() {
        loadItems();
        loadBlocks();
        loadEntities();
    }

    private static void loadItems() {

    }

    private static void loadBlocks() {
        BlockRegistry.register(new BlockTypeAir());
        BlockRegistry.register(new BlockTypeDirt());
        BlockRegistry.register(new BlockTypeGrass());
        BlockRegistry.register(new BlockTypeStone());
    }

    private static void loadEntities() {
        EntityRegistry.register(new HumanEntityType());
        EntityRegistry.register(new CowEntityType());
    }

}
