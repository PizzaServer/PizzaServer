package io.github.willqi.pizzaserver.api.network.protocol.data;

/**
 * Represents a specific item state of a Minecraft version
 */
public interface ItemState {

    /**
     * Get the minecraft id of the item state (e.g. minecraft:air)
     * @return minecraft item id
     */
    String getItemId();

    int getId();

    boolean isComponentBased();

}
