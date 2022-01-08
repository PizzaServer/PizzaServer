package io.github.pizzaserver.api.utils;

import com.nukkitx.math.vector.Vector3f;

public class BoundingBox {

    private final float minX;
    private final float maxX;
    private final float minY;
    private final float maxY;
    private final float minZ;
    private final float maxZ;


    public BoundingBox(BoundingBox boundingBox) {
        this(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ(), boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ());
    }

    public BoundingBox(Vector3f coordinatesA, Vector3f coordinatesB) {
        this(coordinatesA.getX(), coordinatesA.getY(), coordinatesA.getZ(), coordinatesB.getX(), coordinatesB.getY(), coordinatesB.getZ());
    }

    public BoundingBox(float x1, float y1, float z1, float x2, float y2, float z2) {
        this.minX = Math.min(x1, x2);
        this.maxX = Math.max(x1, x2);

        this.minY = Math.min(y1, y2);
        this.maxY = Math.max(y1, y2);

        this.minZ = Math.min(z1, z2);
        this.maxZ = Math.max(z1, z2);
    }

    public float getMinX() {
        return this.minX;
    }

    public float getMinY() {
        return this.minY;
    }

    public float getMinZ() {
        return this.minZ;
    }

    public float getMaxX() {
        return this.maxX;
    }

    public float getMaxY() {
        return this.maxY;
    }

    public float getMaxZ() {
        return this.maxZ;
    }

    public BoundingBox translate(Vector3f translation) {
        return this.translate(translation.getX(), translation.getY(), translation.getZ());
    }

    public BoundingBox translate(float x, float y, float z) {
        return new BoundingBox(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public BoundingBox grow(float amount) {
        return new BoundingBox(this.minX - amount, this.minY - amount, this.minZ - amount, this.maxX + amount, this.maxY + amount, this.maxZ + amount);
    }

    public boolean collidesWith(BoundingBox otherBox) {
        boolean horizontalCollision = this.collidesWithXAxis(otherBox) && this.collidesWithZAxis(otherBox);
        boolean verticalCollision = this.collidesWithYAxis(otherBox);

        return horizontalCollision && verticalCollision;
    }

    public boolean collidesWithXAxis(BoundingBox otherBox) {
        return otherBox.getMaxX() > this.getMinX() && this.getMaxX() > otherBox.getMinX();
    }

    public boolean collidesWithZAxis(BoundingBox otherBox) {
        return otherBox.getMaxZ() > this.getMinZ() && this.getMaxZ() > otherBox.getMinZ();
    }

    public boolean collidesWithYAxis(BoundingBox otherBox) {
        return otherBox.getMaxY() > this.getMinY() && this.getMaxY() > otherBox.getMinY();
    }

    public float calcXOffset(BoundingBox otherBox, float deltaX) {
        if (!(this.collidesWithYAxis(otherBox) && this.collidesWithZAxis(otherBox))) {
            return deltaX;
        }

        if (deltaX > 0 && this.getMaxX() <= otherBox.getMinX()) {
            float difference = otherBox.getMinX() - this.getMaxX();
            if (difference < deltaX) {
                deltaX = difference;
            }
        }
        if (deltaX < 0 && this.getMinX() >= otherBox.getMaxX()) {
            float difference = otherBox.getMaxX() - this.getMinX();
            if (difference > deltaX) {
                deltaX = difference;
            }
        }

        return deltaX;
    }

    public float calcYOffset(BoundingBox otherBox, float deltaY) {
        if (!(this.collidesWithZAxis(otherBox) && this.collidesWithXAxis(otherBox))) {
            return deltaY;
        }

        if (deltaY > 0 && this.getMaxY() <= otherBox.getMinY()) {
            float difference = otherBox.getMinY() - this.getMaxY();
            if (difference < deltaY) {
                deltaY = difference;
            }
        }
        if (deltaY < 0 && this.getMinY() >= otherBox.getMaxY()) {
            float difference = otherBox.getMaxY() - this.getMinY();
            if (difference > deltaY) {
                deltaY = difference;
            }
        }

        return deltaY;
    }

    public float calcZOffset(BoundingBox otherBox, float deltaZ) {
        if (!(this.collidesWithXAxis(otherBox) && this.collidesWithYAxis(otherBox))) {
            return deltaZ;
        }

        if (deltaZ > 0 && this.getMaxZ() <= otherBox.getMinZ()) {
            float difference = otherBox.getMinZ() - this.getMaxZ();
            if (difference < deltaZ) {
                deltaZ = difference;
            }
        }
        if (deltaZ < 0 && this.getMinZ() >= otherBox.getMaxZ()) {
            float difference = otherBox.getMaxZ() - this.getMinZ();
            if (difference > deltaZ) {
                deltaZ = difference;
            }
        }

        return deltaZ;
    }

    @Override
    public String toString() {
        return "BoundingBox(minX=" + this.getMinX()
                + ", maxX=" + this.getMaxX()
                + ", minY=" + this.getMinY()
                + ", maxY=" + this.getMaxY()
                + ", minZ=" + this.getMinZ()
                + ", maxZ=" + this.getMaxZ()
                + ")";
    }

}
