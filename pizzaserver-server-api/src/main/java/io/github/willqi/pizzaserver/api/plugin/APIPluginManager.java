package io.github.willqi.pizzaserver.api.plugin;

import io.github.willqi.pizzaserver.api.plugin.events.APIEvent;

public interface APIPluginManager {

    /**
     * Notify all plugins of an {@link APIEvent}
     * @param event
     */
    void callEvent(APIEvent event);

}
