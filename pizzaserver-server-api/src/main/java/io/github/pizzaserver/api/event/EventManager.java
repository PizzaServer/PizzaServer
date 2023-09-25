package io.github.pizzaserver.api.event;

import io.github.pizzaserver.api.event.filter.EventFilter;

/**
 * Handles registering event listeners and calling events.
 */
public interface EventManager {

    void call(Event event);

    /**
     * Creates a child EventManager which passes all the events its
     * parent EventManager receives, applying its own filters.
     * Events called on the child EventManager are only called on itself
     * and its own children.
     */
    EventManager createChild(EventFilter... filters);

    /**
     * Removes an existing child EventManager from this.
     * It does not deactivate the EventManager, rather preventing events
     * from the parent reaching the specified child.
     */
    void removeChild(EventManager child);

    /**
     * Registers a listener to this EventManager.
     * @param listener the listener to be registered.
     * @param registrarHandle in the case of a plugin registering a listener, this should be the plugin instance
     * @return listener for storing an instance.
     */
    Object addListener(Object listener, Object registrarHandle);

    /**
     * Removes listener from this EventManager and any child EventManager's.
     * @param listener the listener to be removed.
     */
    void removeListener(Object listener);

    /**
     * Removes a listener from the managers listener list.
     * @param listener the listener to be removed.
     * @param removeFromChildren should instances of this listener be removed in child EventManagers?
     */
    void removeListener(Object listener, boolean removeFromChildren);

    /**
     * Removes all listeners that were registered with the given registrar.
     * @param registrarHandle in the case of a plugin removing its listeners, this should be the plugin instance
     */
    void removeListenersFor(Object registrarHandle);

    /**
     * Removes all listeners that were registered with the given registrar.
     * @param registrarHandle in the case of a plugin removing its listeners, this should be the plugin instance
     * @param removeFromChildren should instances of this listener be removed in child EventManagers?
     */
    void removeListenersFor(Object registrarHandle, boolean removeFromChildren);

}
