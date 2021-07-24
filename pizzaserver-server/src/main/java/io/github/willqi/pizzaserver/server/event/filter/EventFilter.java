package io.github.willqi.pizzaserver.server.event.filter;

import io.github.willqi.pizzaserver.server.event.BaseEvent;

public interface EventFilter {

    boolean checkEvent(BaseEvent eventIn);

}
