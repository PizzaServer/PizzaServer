package io.github.willqi.pizzaserver.server.event.type;

public interface CancellableType {

    boolean isCancelled();

    default void setCancelled() { setCancelled(true); }
    void setCancelled(boolean isCancelled);

}
