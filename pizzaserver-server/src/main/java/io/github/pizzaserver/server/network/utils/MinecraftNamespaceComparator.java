package io.github.pizzaserver.server.network.utils;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.item.Item;

import java.nio.charset.StandardCharsets;

/**
 * Minecraft namespaces are sorted by their child key before the parent key.
 * bananas:cow goes before apples:dog
 * This is not a Comparator because we use methods internally to sort by non-string objects that contain a Minecraft namespace.
 * (e.g. sending block properties in the StartGamePacket)
 */
public class MinecraftNamespaceComparator {

    private static final long FNV1_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV1_PRIME_64 = 1099511628211L;


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

    // https://gist.github.com/SupremeMortal/5e09c8b0eb6b3a30439b317b875bc29c
    // Thank you Supreme
    public static int compareFNV(String idA, String idB) {
        byte[] bytes1 = idA.getBytes(StandardCharsets.UTF_8);
        byte[] bytes2 = idB.getBytes(StandardCharsets.UTF_8);
        long hash1 = fnv164(bytes1);
        long hash2 = fnv164(bytes2);
        return Long.compareUnsigned(hash1, hash2);
    }

    private static long fnv164(byte[] data) {
        long hash = FNV1_64_INIT;
        for (byte datum : data) {
            hash *= FNV1_PRIME_64;
            hash ^= (datum & 0xff);
        }

        return hash;
    }

}
