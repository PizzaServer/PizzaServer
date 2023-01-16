package io.github.pizzaserver.commons.data.react;

import java.util.HashSet;
import java.util.function.Consumer;

public class ActionSubscriptions<T> {

    private final HashSet<Consumer<T>> subscribers;

    public ActionSubscriptions() {
        this.subscribers = new HashSet<>();
    }


    public void add(Consumer<T> subscriber) {
        this.subscribers.add(subscriber);
    }

    public void remove(Consumer<T> subscriber) {
        this.subscribers.remove(subscriber);
    }

    public void clear() {
        this.subscribers.clear();
    }

    public void emit(T data) {
        subscribers.forEach(sub -> {
            try { sub.accept(data); }
            catch (Exception err) { err.printStackTrace(); }
        });
    }

}
