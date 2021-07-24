package io.github.willqi.pizzaserver.server.plugin;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.plugin.PluginManager;
import io.github.willqi.pizzaserver.api.plugin.event.BaseEvent;

public class BedrockPluginManager implements PluginManager {

    private final Server server;

    public BedrockPluginManager(Server server) {
        this.server = server;
    }

    @Override
    public void callEvent(BaseEvent event) {

    }

}
