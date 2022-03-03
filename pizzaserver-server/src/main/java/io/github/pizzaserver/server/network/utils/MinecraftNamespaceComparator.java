package io.github.pizzaserver.server.network.utils;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.item.Item;

/**
 * Minecraft namespaces are sorted by their child key before the parent key.
 * bananas:cow goes before apples:dog
 * This is not a Comparator because we use methods internally to sort by non-string objects that contain a Minecraft namespace.
 * (e.g. sending block properties in the StartGamePacket)
 */
public class MinecraftNamespaceComparator {

    public static int compareBlocks(Block blockA, Block blockB) {
        return compare(blockA.getBlockId(), blockB.getBlockId());
    }

    public static int compareItems(Item itemA, Item itemB) {
        return compare(itemA.getItemId(), itemB.getItemId());
    }

    public static int compare(String idA, String idB) {
        String childIdA = idA.substring(idA.indexOf(":") + 1);
        String childIdB = idB.substring(idB.indexOf(":") + 1);

        // Compare by child first
        int childIdComparsion = childIdB.compareTo(childIdA);
        if (childIdComparsion != 0) {
            return childIdComparsion;
        }

        // Compare by namespace if childs are equal
        String namespaceA = idA.substring(0, idA.indexOf(":"));
        String namespaceB = idB.substring(0, idB.indexOf(":"));
        return namespaceB.compareTo(namespaceA);
    }

}
