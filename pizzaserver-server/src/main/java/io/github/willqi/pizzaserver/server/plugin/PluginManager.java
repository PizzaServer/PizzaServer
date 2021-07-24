package io.github.willqi.pizzaserver.server.plugin;

import io.github.willqi.pizzaserver.api.APIServer;
import io.github.willqi.pizzaserver.api.plugin.APIPluginManager;
import io.github.willqi.pizzaserver.api.plugin.events.APIEvent;

public class PluginManager implements APIPluginManager {

    private final APIServer server;

    public PluginManager(APIServer server) {
        this.server = server;
    }

    @Override
    public void callEvent(APIEvent event) {

    }

}
