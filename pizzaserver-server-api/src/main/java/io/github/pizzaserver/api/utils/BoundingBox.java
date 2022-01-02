package io.github.pizzaserver.api.utils;

import com.nukkitx.math.vector.Vector3f;

public class BoundingBox implements Cloneable {

    private float height;
    private float width;

    private float x;
    private float y;
    private float z;


    public Vector3f getPosition() {
        return Vector3f.from(this.x, this.y, this.z);
    }

    public void setPosition(Vector3f position) {
        this.setPosition(position.getX(), position.getY(), position.getZ());
    }

    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Retrieve the height of this bounding box from the position up.
     * @return height of the bounding box from the position up.
     */
    public float getHeight() {
        return this.height;
    }

    /**
     * Set the height of this bounding box from the position up.
     * @param height of the bounding box from the position up.
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Retrieve the total width of this bounding box.
     * @return total width of bounding box
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * Set the total width of this bounding box.
     * @param width total width of bounding box
     */
    public void setWidth(float width) {
        this.width = width;
    }

    public boolean collidesWith(BoundingBox otherBox) {
        boolean horizontalCollision = this.collidesWithXAxis(otherBox) && this.collidesWithZAxis(otherBox);
        boolean verticalCollision = this.collidesWithYAxis(otherBox);

        return horizontalCollision && verticalCollision;
    }

    public boolean collidesWithXAxis(BoundingBox otherBox) {
        return Math.abs(otherBox.getPosition().getX() - this.x) < (this.getWidth() + otherBox.getWidth()) / 2;
    }

    public boolean collidesWithZAxis(BoundingBox otherBox) {
        return Math.abs(otherBox.getPosition().getZ() - this.z) < (this.getWidth() + otherBox.getWidth()) / 2;
    }

    public boolean collidesWithYAxis(BoundingBox otherBox) {
        BoundingBox lowerBox;
        BoundingBox higherBox;
        if (this.getPosition().getY() > otherBox.getPosition().getY()) {
            lowerBox = otherBox;
            higherBox = this;
        } else {
            lowerBox = this;
            higherBox = otherBox;
        }
        return lowerBox.getPosition().getY() + lowerBox.getHeight() > higherBox.getPosition().getY();
    }

    @Override
    public BoundingBox clone() {
        BoundingBox boundingBox = new BoundingBox();
        boundingBox.setPosition(this.getPosition());
        boundingBox.setWidth(this.getWidth());
        boundingBox.setHeight(this.getHeight());
        return boundingBox;
    }

}
