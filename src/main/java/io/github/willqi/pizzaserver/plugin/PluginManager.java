package io.github.willqi.pizzaserver.plugin;

import io.github.willqi.pizzaserver.Server;
import io.github.willqi.pizzaserver.events.Event;

public class PluginManager {

    private Server server;

    public PluginManager(Server server) {
        this.server = server;
    }

    public void callEvent(Event event) {

    }

}
