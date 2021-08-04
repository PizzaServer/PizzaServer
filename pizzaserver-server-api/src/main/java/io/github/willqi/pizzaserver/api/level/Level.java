package io.github.willqi.pizzaserver.api.level;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.commons.world.Dimension;

import java.io.IOException;

public interface Level {

    String getName();

    Server getServer();

    World getDimension(Dimension dimension);

    void save() throws IOException;

}
