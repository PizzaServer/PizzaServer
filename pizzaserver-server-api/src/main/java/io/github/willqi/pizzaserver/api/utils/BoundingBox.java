package io.github.willqi.pizzaserver.api.utils;

import io.github.willqi.pizzaserver.commons.utils.Vector3;

public class BoundingBox {

    private float height;
    private float width;

    private float x;
    private float y;
    private float z;


    public Vector3 getPosition() {
        return new Vector3(this.x, this.y, this.z);
    }

    public void setPosition(Vector3 position) {
        this.setPosition(position.getX(), position.getY(), position.getZ());
    }

    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    private double horizontalDistanceBetween(BoundingBox otherBox) {
        Vector3 otherPosition = otherBox.getPosition();
        return Math.sqrt(Math.pow(this.x - otherPosition.getX(), 2)
                + Math.pow(this.z - otherPosition.getZ(), 2));
    }

    public boolean collidesWith(BoundingBox otherBox) {
        float maxHorizontalHypo = this.getWidth() * otherBox.getWidth();
        boolean horizontalCollision = this.horizontalDistanceBetween(otherBox) < maxHorizontalHypo;

        double yDiff = Math.abs(this.y - otherBox.getPosition().getY());
        float minHeight = Math.min(this.getHeight(), otherBox.getHeight());
        boolean verticalCollision = yDiff < minHeight;

        return horizontalCollision && verticalCollision;
    }

}
