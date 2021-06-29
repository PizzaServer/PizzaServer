package io.github.willqi.pizzaserver.server.world;

import io.github.willqi.pizzaserver.mcworld.MCWorld;
import io.github.willqi.pizzaserver.server.Server;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class World implements Closeable {

    private final Server server;
    private final MCWorld mcWorld;


    public World(Server server, File worldDirectory) throws IOException {
        this.mcWorld = new MCWorld(worldDirectory);
        this.server = server;
    }

    public Server getServer() {
        return this.server;
    }

    @Override
    public void close() throws IOException {
        this.mcWorld.close();
    }

}
