package io.github.pizzaserver.api.item;

public abstract class CustomItem extends Item {

    public CustomItem(String itemId, int count) {
        super(itemId, count);
    }

    public CustomItem(String itemId, int count, int meta) {
        super(itemId, count, meta);
    }

    /**
     * The name of the icon provided in the minecraft:icon component.
     *
     * @return minecraft:icon value
     */
    public abstract String getIconName();

    /**
     * Returns if this item should be held visually like a tool.
     *
     * @return if the item should be held visually like a tool
     */
    public boolean isHandEquipped() {
        return false;
    }

    /**
     * Returns if this item should appear enchanted.
     *
     * @return if the item should appear enchanted
     */
    public boolean hasFoil() {
        return false;
    }

    /**
     * Returns if this item should appear flipped on the client.
     *
     * @return if this item should appear flipped on the client
     */
    public boolean isMirroredArt() {
        return false;
    }

}
