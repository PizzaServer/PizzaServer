package io.github.willqi.pizzaserver.server.network.utils;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinecraftNamespaceComparatorTest {

    @Test
    public void shouldOrderChildIdsCorrectly() {
        assertEquals(1, MinecraftNamespaceComparator.compareNamespaces("minecraft:apples", "minecraft:bananas"));
        assertEquals(-1, MinecraftNamespaceComparator.compareNamespaces("minecraft:bananas", "minecraft:apples"));

        assertEquals(-1, MinecraftNamespaceComparator.compareNamespaces("alast:bananas", "zfirst:apples"));
        assertEquals(1, MinecraftNamespaceComparator.compareNamespaces("bparent:apples", "aparent:bananas"));
    }

    @Test
    public void shouldOrderNamespacesCorrectly() {
        assertEquals(1, MinecraftNamespaceComparator.compareNamespaces("a:element", "b:element"));
        assertEquals(-1, MinecraftNamespaceComparator.compareNamespaces("b:element", "a:element"));
    }

}
