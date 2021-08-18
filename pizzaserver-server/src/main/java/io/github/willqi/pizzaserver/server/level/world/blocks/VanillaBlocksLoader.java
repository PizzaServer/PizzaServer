package io.github.willqi.pizzaserver.server.level.world.blocks;

import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.server.level.world.blocks.types.*;

public class VanillaBlocksLoader {

    public static void load() {
        BlockRegistry.register(new BlockTypeAir());
        BlockRegistry.register(new BlockTypeDirt());
        BlockRegistry.register(new BlockTypeGrass());
        BlockRegistry.register(new BlockTypeStone());
    }

}
