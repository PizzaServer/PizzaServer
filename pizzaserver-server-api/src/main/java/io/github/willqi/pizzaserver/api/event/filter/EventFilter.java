package io.github.willqi.pizzaserver.api.event.filter;

import io.github.willqi.pizzaserver.api.event.Event;

public interface EventFilter {

    boolean checkEvent(Event eventIn);

}
