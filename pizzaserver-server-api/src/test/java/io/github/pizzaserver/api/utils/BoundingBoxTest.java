package io.github.pizzaserver.api.utils;

import io.github.pizzaserver.api.utils.BoundingBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoundingBoxTest {

    @Test
    public void shouldInterceptOnAllAxis() {
        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setPosition(0, 0, 0);
        boundingBox.setWidth(2);
        boundingBox.setHeight(1);

        BoundingBox otherBox = new BoundingBox();
        otherBox.setPosition(1.4f, 0.9f, -1.4f);
        otherBox.setWidth(1);
        otherBox.setHeight(0.5f);

        assertTrue(boundingBox.collidesWith(otherBox));
    }

    @Test
    public void shouldNotInterceptOnAllAxis() {
        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setPosition(0, 0, 0);
        boundingBox.setWidth(2);
        boundingBox.setHeight(1);

        BoundingBox otherBox = new BoundingBox();
        otherBox.setPosition(1.5f, 1.5f, -1.5f);
        otherBox.setWidth(1);
        otherBox.setHeight(0.5f);

        assertFalse(boundingBox.collidesWith(otherBox));
    }

    @Test
    public void shouldInterceptOnXAxis() {
        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setPosition(0, 0, 0);
        boundingBox.setWidth(1);
        boundingBox.setHeight(1);

        BoundingBox otherBox = new BoundingBox();
        otherBox.setPosition(1, 0, 0);
        otherBox.setWidth(1.01f);
        otherBox.setHeight(1);

        assertTrue(boundingBox.collidesWith(otherBox) && boundingBox.collidesWithXAxis(otherBox));
    }

    @Test
    public void shouldNotInterceptOnXAxis() {
        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setPosition(0, 0, 0);
        boundingBox.setWidth(1f);
        boundingBox.setHeight(1);

        BoundingBox otherBox = new BoundingBox();
        otherBox.setPosition(1, 0, 0);
        otherBox.setWidth(1);
        otherBox.setHeight(1);

        assertFalse(boundingBox.collidesWith(otherBox) || boundingBox.collidesWithXAxis(otherBox));
    }

    @Test
    public void shouldInterceptOnYAxis() {
        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setPosition(0, 0.5f, 0);
        boundingBox.setWidth(1);
        boundingBox.setHeight(0.1f);

        BoundingBox otherBox = new BoundingBox();
        otherBox.setPosition(0, 0, 0);
        otherBox.setWidth(1);
        otherBox.setHeight(0.6f);

        assertTrue(boundingBox.collidesWith(otherBox) && boundingBox.collidesWithYAxis(otherBox));
    }

    @Test
    public void shouldNotInterceptOnYAxisDueToHeightOnlyGoingUp() {
        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setPosition(0, 0, 0);
        boundingBox.setWidth(1);
        boundingBox.setHeight(0.49f);

        BoundingBox otherBox = new BoundingBox();
        otherBox.setPosition(0, 0.5f, 0);
        otherBox.setWidth(1);
        otherBox.setHeight(0.5f);

        assertFalse(boundingBox.collidesWith(otherBox) || boundingBox.collidesWithYAxis(otherBox));
    }

    @Test
    public void shouldNotInterceptOnYAxisDueToBeingTooSmall() {
        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setPosition(0, 0, 0);
        boundingBox.setWidth(1);
        boundingBox.setHeight(0.5f);

        BoundingBox otherBox = new BoundingBox();
        otherBox.setPosition(0, 0.5f, 0);
        otherBox.setWidth(1);
        otherBox.setHeight(0.5f);

        assertFalse(boundingBox.collidesWith(otherBox) || boundingBox.collidesWithYAxis(otherBox));
    }

    @Test
    public void shouldInterceptOnZAxis() {
        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setPosition(0, 0, 0);
        boundingBox.setWidth(1);
        boundingBox.setHeight(1);

        BoundingBox otherBox = new BoundingBox();
        otherBox.setPosition(0, 0, 1);
        otherBox.setWidth(1.01f);
        otherBox.setHeight(1);

        assertTrue(boundingBox.collidesWith(otherBox) && boundingBox.collidesWithZAxis(otherBox));
    }

    @Test
    public void shouldNotInterceptOnZAxis() {
        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setPosition(0, 0, 0);
        boundingBox.setWidth(1);
        boundingBox.setHeight(1);

        BoundingBox otherBox = new BoundingBox();
        otherBox.setPosition(0, 0, 1);
        otherBox.setWidth(1);
        otherBox.setHeight(1);

        assertFalse(boundingBox.collidesWith(otherBox) || boundingBox.collidesWithZAxis(otherBox));
    }

}
