package io.github.willqi.pizzaserver.server.network.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinecraftNamespaceComparatorTest {

    @Test
    public void shouldOrderChildIdsCorrectly() {
        assertEquals(1, MinecraftNamespaceComparator.compare("minecraft:apples", "minecraft:bananas"));
        assertEquals(-1, MinecraftNamespaceComparator.compare("minecraft:bananas", "minecraft:apples"));

        assertEquals(-1, MinecraftNamespaceComparator.compare("alast:bananas", "zfirst:apples"));
        assertEquals(1, MinecraftNamespaceComparator.compare("bparent:apples", "aparent:bananas"));
    }

    @Test
    public void shouldOrderNamespacesCorrectly() {
        assertEquals(1, MinecraftNamespaceComparator.compare("a:element", "b:element"));
        assertEquals(-1, MinecraftNamespaceComparator.compare("b:element", "a:element"));
    }

}
