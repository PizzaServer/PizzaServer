package io.github.pizzaserver.commons.data.react;

import java.util.HashSet;
import java.util.function.Consumer;

public class ActionSubscriptions<T> {

    private final HashSet<Consumer<T>> subscribers;
    private final HashSet<Runnable> observers;

    public ActionSubscriptions() {
        this.subscribers = new HashSet<>();
        this.observers = new HashSet<>();
    }


    public void add(Consumer<T> subscriber) {
        this.subscribers.add(subscriber);
    }

    public void add(Runnable observer) {
        this.observers.add(observer);
    }

    public void remove(Consumer<?> subscriber) {
        this.subscribers.remove(subscriber);
    }

    public void remove(Runnable observer) {
        this.observers.remove(observer);
    }

    public void clear() {
        this.subscribers.clear();
        this.observers.clear();
    }

    public void emit(T data) {
        subscribers.forEach(sub -> {
            try { sub.accept(data); }
            catch (Exception err) { err.printStackTrace(); }
        });

        observers.forEach(sub -> {
            try { sub.run(); }
            catch (Exception err) { err.printStackTrace(); }
        });
    }

}
