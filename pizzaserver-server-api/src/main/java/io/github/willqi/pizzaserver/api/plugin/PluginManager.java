package io.github.willqi.pizzaserver.api.plugin;

import io.github.willqi.pizzaserver.api.plugin.event.BaseEvent;

public interface PluginManager {

    /**
     * Notify all plugins of an {@link BaseEvent}
     * @param event
     */
    void callEvent(BaseEvent event);

}
