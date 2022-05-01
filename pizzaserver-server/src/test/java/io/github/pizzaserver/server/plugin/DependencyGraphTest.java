package io.github.pizzaserver.server.plugin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DependencyGraphTest {

    @Test
    public void cyclicDependency() {
        DependencyGraph g1 = new DependencyGraph();
        g1.queue("A", List.of("B"));
        g1.queue("B", List.of("A"));
        Assertions.assertThrows(RuntimeException.class, g1::finish);

        g1.queue("C", List.of("A", "B"));
        Assertions.assertThrows(RuntimeException.class, g1::finish);

        DependencyGraph g2 = new DependencyGraph();
        g2.queue("A", List.of("B"));
        g2.queue("B", List.of("C"));
        g2.queue("C", List.of("A"));
        Assertions.assertThrows(RuntimeException.class, g2::finish);
    }

    @Test
    public void correctness() {
        DependencyGraph g1 = new DependencyGraph();
        g1.queue("A", List.of("B"));
        g1.queue("B", List.of());

        Assertions.assertEquals(List.of("B", "A"), g1.finish());
        g1.test(g1.finish());

        g1.queue("C", List.of());

        Assertions.assertEquals(List.of("B", "A", "C"), g1.finish());
        g1.test(g1.finish());
    }

    @Test
    public void order() {
        DependencyGraph g1 = new DependencyGraph();
        g1.queue("A", List.of());
        g1.queue("B", List.of());
        g1.queue("C", List.of());
        g1.queue("D", List.of());
        g1.queue("E", List.of());

        Assertions.assertEquals(List.of("A", "B", "C", "D", "E"), g1.finish());
        g1.test(g1.finish());
    }

    @Test
    public void transient_() {
        DependencyGraph g1 = new DependencyGraph();
        g1.queue("A", List.of("C"));
        g1.queue("B", List.of());
        g1.queue("C", List.of("B"));
        g1.queue("D", List.of("C"));

        Assertions.assertEquals(List.of("B", "C", "A", "D"), g1.finish());
        g1.test(g1.finish());
    }

    @Test
    public void complex() {
        DependencyGraph g1 = new DependencyGraph();
        g1.queue("A", List.of("B", "C"));
        g1.queue("B", List.of("B"));
        g1.queue("C", List.of("F"));
        g1.queue("D", List.of("C"));
        g1.queue("E", List.of("B"));
        g1.queue("F", List.of("B", "E"));

        Assertions.assertEquals(List.of("B", "E", "F", "C", "A", "D"), g1.finish());
        g1.test(g1.finish());

    }

    @Test
    public void complexOptional() {
        DependencyGraph g1 = new DependencyGraph();
        g1.queue("A", List.of("B", "C"));
        g1.queue("B", List.of("B"));
        g1.queue("C", List.of("F"), List.of("F"));
        g1.queue("D", List.of("C"));
        g1.queue("E", List.of("B"));
        //g1.queue("F", List.of("B", "E"));

        Assertions.assertEquals(List.of("B", "C", "A", "D", "E"), g1.finish());
        g1.test(g1.finish());
    }

    @Test
    public void optional() {
        DependencyGraph g1 = new DependencyGraph();
        g1.queue("A", List.of("B", "C"));
        g1.queue("B", List.of());
        g1.queue("C", List.of("D"), List.of("D"));

        Assertions.assertEquals(List.of("B", "C", "A"), g1.finish());
        g1.test(g1.finish());
    }

}
