package io.github.pizzaserver.commons.data.react;

import java.util.function.Consumer;

public interface ActionSource {

    /**
     * Registers a new callback for a given action, consuming any events emitted under
     * the action in the future.
     * @param action the action's key.
     * @param callback the method/consumer that handles the action event.
     * @param <T> the expected type of the action's payload
     */
    <T> void listenFor(ActionType<T> action, Consumer<T> callback);

    /**
     * Registers a new callback for a given action, observing any events emitted under
     * the action in the future.
     * @param action the action's key.
     * @param callback the method that reactions to the action event.
     */
    <T> void listenFor(ActionType<T> action, Runnable callback);

    /**
     * Unregisters a previously registered callback for a given action, stopping it from
     * consuming any events related to the action. If it wasn't previously registered, this
     * method silently fails.
     * @param action the action's key.
     * @param callback the method/consumer that was assigned to handle the given action.
     */
    void stopListeningFor(ActionType<?> action, Consumer<?> callback);

    /**
     * Unregisters a previously registered callback for a given action, stopping it from
     * observing any events related to the action. If it wasn't previously registered, this
     * method silently fails.
     * @param action the action's key.
     * @param callback the method that was assigned to handle the given action.
     */
    void stopListeningFor(ActionType<?> action, Runnable callback);
}
