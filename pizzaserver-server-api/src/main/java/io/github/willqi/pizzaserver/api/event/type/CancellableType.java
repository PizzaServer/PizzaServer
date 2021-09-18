package io.github.willqi.pizzaserver.api.event.type;

public interface CancellableType {

    boolean isCancelled();

    default void setCancelled() {
        this.setCancelled(true);
    }

    void setCancelled(boolean isCancelled);

}
