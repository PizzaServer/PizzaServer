package io.github.willqi.pizzaserver.server.plugin;

import io.github.willqi.pizzaserver.api.APIServer;
import io.github.willqi.pizzaserver.api.plugin.APIPluginManager;
import io.github.willqi.pizzaserver.api.plugin.event.BaseEvent;

public class PluginManager implements APIPluginManager {

    private final APIServer server;

    public PluginManager(APIServer server) {
        this.server = server;
    }

    @Override
    public void callEvent(BaseEvent event) {

    }

}
