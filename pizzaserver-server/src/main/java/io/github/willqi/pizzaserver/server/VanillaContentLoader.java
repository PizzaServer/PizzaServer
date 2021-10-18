package io.github.willqi.pizzaserver.server;

import io.github.willqi.pizzaserver.api.entity.EntityRegistry;
import io.github.willqi.pizzaserver.api.entity.definition.components.handlers.EntityScaleComponentHandler;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityScaleComponent;
import io.github.willqi.pizzaserver.api.entity.definition.impl.CowEntityDefinition;
import io.github.willqi.pizzaserver.api.entity.definition.impl.HumanEntityDefinition;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.server.level.world.blocks.types.*;

public class VanillaContentLoader {

    public static void load() {
        loadItems();
        loadBlocks();
        loadEntityComponents();
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

    private static void loadEntityComponents() {
        EntityRegistry.registerComponent(EntityScaleComponent.class, new EntityScaleComponent(1), new EntityScaleComponentHandler());
    }

    private static void loadEntities() {
        EntityRegistry.registerDefinition(new HumanEntityDefinition());
        EntityRegistry.registerDefinition(new CowEntityDefinition());
    }

}
