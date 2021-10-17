package io.github.willqi.pizzaserver.server;

import io.github.willqi.pizzaserver.api.entity.EntityRegistry;
import io.github.willqi.pizzaserver.api.entity.definition.impl.CowEntityDefinition;
import io.github.willqi.pizzaserver.api.entity.definition.impl.HumanEntityDefinition;
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
        EntityRegistry.registerDefinition(new HumanEntityDefinition());
        EntityRegistry.registerDefinition(new CowEntityDefinition());
    }

}
