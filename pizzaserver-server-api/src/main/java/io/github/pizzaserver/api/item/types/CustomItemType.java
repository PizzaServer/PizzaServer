package io.github.pizzaserver.api.item.types;

public interface CustomItemType extends ItemType {

    /**
     * The name of the icon provided in the minecraft:icon component.
     *
     * @return minecraft:icon value
     */
    String getIconName();

}
