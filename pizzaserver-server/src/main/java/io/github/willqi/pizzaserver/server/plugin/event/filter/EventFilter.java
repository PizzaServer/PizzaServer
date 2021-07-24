package io.github.willqi.pizzaserver.server.plugin.event.filter;

import io.github.willqi.pizzaserver.api.plugin.event.BaseEvent;

public interface EventFilter {

    boolean checkEvent(BaseEvent eventIn);

}
