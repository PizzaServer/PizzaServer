package io.github.willqi.pizzaserver.server.plugin;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.plugin.PluginManager;

public class ImplPluginManager implements PluginManager {

    private final Server server;


    public ImplPluginManager(Server server) {
        this.server = server;
    }

}
