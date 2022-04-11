package io.github.pizzaserver.api.item.descriptors;

import io.github.pizzaserver.api.item.Item;

public interface CustomItem extends Item {

    /**
     * The name of the icon provided in the minecraft:icon component.
     *
     * @return minecraft:icon value
     */
    String getIconName();

    /**
     * Returns if this item should be held visually like a tool.
     *
     * @return if the item should be held visually like a tool
     */
    default boolean isHandEquipped() {
        return false;
    }

    /**
     * Returns if this item should appear enchanted.
     *
     * @return if the item should appear enchanted
     */
    default boolean hasFoil() {
        return false;
    }

    /**
     * Returns if this item should appear flipped on the client.
     *
     * @return if this item should appear flipped on the client
     */
    default boolean isMirroredArt() {
        return false;
    }

}
