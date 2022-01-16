package io.github.pizzaserver.server.plugin;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.plugin.PluginManager;

public class ImplPluginManager implements PluginManager {

    private final Server server;


    public ImplPluginManager(Server server) {
        this.server = server;
    }
}
