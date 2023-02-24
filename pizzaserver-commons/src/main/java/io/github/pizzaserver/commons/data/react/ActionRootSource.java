package io.github.pizzaserver.commons.data.react;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

public class ActionRootSource implements ActionSource {

    private final HashMap<ActionType<?>, ActionSubscriptions<?>> subscriptionLists;

    public ActionRootSource() {
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


    public <T> void listenFor(ActionType<T> action, Consumer<T> callback) {
        this.getActionSubscribersOrCreateFor(action)
                .add(callback);
    }

    public <T> void listenFor(ActionType<T> action, Runnable callback) {
        this.getActionSubscribersOrCreateFor(action)
                .add(callback);
    }

    public void stopListeningFor(ActionType<?> action, Consumer<?> callback) {
        this.getActionSubscribersFor(action)
                .ifPresent(subs -> subs.remove(callback));
    }

    public void stopListeningFor(ActionType<?> action, Runnable callback) {
        this.getActionSubscribersFor(action)
                .ifPresent(subs -> subs.remove(callback));
    }

    @SuppressWarnings("unchecked")
    protected <T> Optional<ActionSubscriptions<T>> getActionSubscribersFor(ActionType<T> type) {
        if(!subscriptionLists.containsKey(type)) return Optional.empty();

        ActionSubscriptions<?> list = subscriptionLists.get(type);
        return Optional.of((ActionSubscriptions<T>) list);
    }

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
