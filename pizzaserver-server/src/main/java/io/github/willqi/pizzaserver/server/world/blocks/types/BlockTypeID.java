package io.github.willqi.pizzaserver.server.world.blocks.types;

import io.github.willqi.pizzaserver.commons.data.id.Identifier;
import io.github.willqi.pizzaserver.commons.data.id.Namespace;

/**
 * Contains all registered Vanilla block ids
 */
public class BlockTypeID {

    private static final Namespace VANILLA = new Namespace("minecraft");

    public static final Identifier AIR = VANILLA.id("air");
    public static final Identifier DIRT = VANILLA.id("dirt");
    public static final Identifier GRASS = VANILLA.id("grass");
    public static final Identifier STONE = VANILLA.id("stone");

}
