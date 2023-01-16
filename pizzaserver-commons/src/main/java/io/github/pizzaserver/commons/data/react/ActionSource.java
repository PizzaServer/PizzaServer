package io.github.pizzaserver.commons.data.react;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

public class ActionSource {

    private final HashMap<ActionType<?>, ActionSubscriptions<?>> subscriptionLists;

    public ActionSource() {
        this.subscriptionLists = new HashMap<>();
    }

    /**
     * Broadcasts an action event to any already subscribed callbacks to that
     * specific action.
     * @param type the action's key.
     * @param payload data related to the action.
     * @param <T> the type that the payload should inherit from, defined by the key.
     */
    protected <T> void broadcast(ActionType<T> type, T payload) {
        if(this.getSubscriptionLists().isEmpty()) return;
        this.getActionSubscribersFor(type)
                .ifPresent(subscriptions -> subscriptions.emit(payload));
    }

    /**
     * Registers a new callback for a given action, consuming any events emitted under
     * the action in the future.
     * @param action the action's key.
     * @param callback the method/consumer that handles the action event.
     * @param <T> the expected type of the action's payload
     */
    public <T> void listenFor(ActionType<T> action, Consumer<T> callback) {
        this.getActionSubscribersOrCreateFor(action)
                .add(callback);
    }

    /**
     * Unregisters a previously registered callback for a given action, stopping it from
     * consuming any events related to the action. If it wasn't previously registered, this
     * method silently fails.
     * @param action the action's key.
     * @param callback the method/consumer that was assigned to handle the given action.
     */
    public void stopListeningFor(ActionType<?> action, Consumer<?> callback) {
        this.getActionSubscribersFor(action)
                .ifPresent(subs -> subs.remove(callback));
    }


    /**
     * Returns the action's subscriber/callback list for a given action type.
     * @param type the type assigned to the subscribers of the list.
     * @return the list of subscribers for the action, unless one does not exist - wrapped in an optional.
     * @param <T> the type of the action's payload.
     */
    @SuppressWarnings("unchecked")
    protected <T> Optional<ActionSubscriptions<T>> getActionSubscribersFor(ActionType<T> type) {
        if(!subscriptionLists.containsKey(type)) return Optional.empty();

        ActionSubscriptions<?> list = subscriptionLists.get(type);
        return Optional.of((ActionSubscriptions<T>) list);
    }

    /**
     * Returns the action's subscriber/callback list for a given action type, creating a new
     * one if one does not already exist.
     * @param type the type assigned to the subscribers of the list.
     * @return the only list of subscribers for the action.
     * @param <T> the type of the action's payload.
     */
    protected <T> ActionSubscriptions<T> getActionSubscribersOrCreateFor(ActionType<T> type) {
        Optional<ActionSubscriptions<T>> subList = this.getActionSubscribersFor(type);
        ActionSubscriptions<T> list;

        if (subList.isEmpty()) {
            list = new ActionSubscriptions<>();
            this.getSubscriptionLists().put(type, list);

        } else list = subList.get();

        return list;
    }

    protected void clearSubscribers() {
        this.subscriptionLists.clear();
    }


    protected HashMap<ActionType<?>, ActionSubscriptions<?>> getSubscriptionLists() {
        return this.subscriptionLists;
    }
}
