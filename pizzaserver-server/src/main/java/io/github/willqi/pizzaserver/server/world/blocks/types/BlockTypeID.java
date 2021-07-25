package io.github.willqi.pizzaserver.server.world.blocks.types;

import io.github.willqi.pizzaserver.commons.data.id.Identifier;
import io.github.willqi.pizzaserver.commons.data.id.Namespace;
import io.github.willqi.pizzaserver.commons.data.storage.IdentityKey;
import io.github.willqi.pizzaserver.server.world.blocks.types.impl.BlockTypeAir;
import io.github.willqi.pizzaserver.server.world.blocks.types.impl.BlockTypeDirt;
import io.github.willqi.pizzaserver.server.world.blocks.types.impl.BlockTypeGrass;
import io.github.willqi.pizzaserver.server.world.blocks.types.impl.BlockTypeStone;

/**
 * Contains all registered Vanilla block ids
 */
public class BlockTypeID {

    private static final Namespace VANILLA = new Namespace("minecraft");

    public static final IdentityKey<BlockTypeAir> AIR = IdentityKey.of(VANILLA.id("air"));
    public static final IdentityKey<BlockTypeDirt> DIRT = IdentityKey.of(VANILLA.id("dirt"));
    public static final IdentityKey<BlockTypeGrass> GRASS = IdentityKey.of(VANILLA.id("grass"));
    public static final IdentityKey<BlockTypeStone> STONE = IdentityKey.of(VANILLA.id("stone"));

}
