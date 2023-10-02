package io.github.pizzaserver.api.utils;

import org.cloudburstmc.math.vector.Vector3f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoundingBoxTest {

    @Test
    public void shouldInterceptOnAllAxis() {
        BoundingBox boundingBox = new BoundingBox(Vector3f.from(-2, 1, -2), Vector3f.from(2, 0, 2));
        BoundingBox otherBox = new BoundingBox(Vector3f.from(-1, 0.5f, -1), Vector3f.from(3, 0, -1));
        assertTrue(boundingBox.collidesWith(otherBox));
    }

    @Test
    public void shouldNotInterceptOnAllAxis() {
        BoundingBox boundingBox = new BoundingBox(Vector3f.from(-2, 0, -2), Vector3f.from(2, 1, 2));
        BoundingBox otherBox = new BoundingBox(Vector3f.from(-3, 0.5f, 0), Vector3f.from(-2f, 0, 2));
        assertFalse(boundingBox.collidesWith(otherBox));
    }

    @Test
    public void shouldInterceptOnXAxis() {
        BoundingBox boundingBox = new BoundingBox(Vector3f.from(-1, 1, -1), Vector3f.from(1, 0, 1));

        assertTrue(boundingBox.collidesWithXAxis(boundingBox.translate(Vector3f.from(1, 0, 0)))
                                && boundingBox.collidesWithXAxis(boundingBox.translate(Vector3f.from(-1, 0, 0))));
    }

    @Test
    public void shouldNotInterceptOnXAxis() {
        BoundingBox boundingBox = new BoundingBox(Vector3f.from(-1, 1, -1), Vector3f.from(1, 0, 1));

        assertFalse(boundingBox.collidesWithXAxis(boundingBox.translate(Vector3f.from(2, 0, 0)))
                        || boundingBox.collidesWithXAxis(boundingBox.translate(Vector3f.from(-2, 0, 0))));
    }

    @Test
    public void shouldInterceptOnYAxis() {
        BoundingBox boundingBox = new BoundingBox(Vector3f.from(-1, 1, -1), Vector3f.from(1, 0, 1));

        assertTrue(boundingBox.collidesWithYAxis(boundingBox.translate(Vector3f.from(0, 0.99f, 0)))
                && boundingBox.collidesWithYAxis(boundingBox.translate(Vector3f.from(0, -0.99f, 0))));
    }

    @Test
    public void shouldNotInterceptOnYAxis() {
        BoundingBox boundingBox = new BoundingBox(Vector3f.from(-1, 1, -1), Vector3f.from(1, 0, 1));

        assertFalse(boundingBox.collidesWithYAxis(boundingBox.translate(Vector3f.from(0, 1, 0)))
                || boundingBox.collidesWithYAxis(boundingBox.translate(Vector3f.from(0, -1, 0))));
    }

    @Test
    public void shouldInterceptOnZAxis() {
        BoundingBox boundingBox = new BoundingBox(Vector3f.from(-1, 1, -1), Vector3f.from(1, 0, 1));

        assertTrue(boundingBox.collidesWithZAxis(boundingBox.translate(Vector3f.from(0, 0, 1)))
                && boundingBox.collidesWithZAxis(boundingBox.translate(Vector3f.from(0, 0, -1))));
    }

    @Test
    public void shouldNotInterceptOnZAxis() {
        BoundingBox boundingBox = new BoundingBox(Vector3f.from(-1, 1, -1), Vector3f.from(1, 0, 1));

        assertFalse(boundingBox.collidesWithZAxis(boundingBox.translate(Vector3f.from(0, 0, 2)))
                || boundingBox.collidesWithZAxis(boundingBox.translate(Vector3f.from(0, 0, -2))));
    }

}
