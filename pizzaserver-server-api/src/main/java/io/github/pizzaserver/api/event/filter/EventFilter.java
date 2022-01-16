package io.github.pizzaserver.api.event.filter;

import io.github.pizzaserver.api.event.Event;

public interface EventFilter {

    boolean checkEvent(Event eventIn);
}
