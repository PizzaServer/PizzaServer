package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.BoundingBox;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

public class EntityPhysicsEngine {

    protected final ImplEntity entity;
    protected boolean updatePosition = true;

    protected Vector3 velocity = new Vector3(0, 0, 0);
    protected Vector3 airAcceleration = new Vector3(0, -0.05f, 0);
    protected float terminalYVelocity = 0.4f;


    public EntityPhysicsEngine(ImplEntity entity) {
        this.entity = entity;
    }

    public Vector3 getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

    /**
     * Retrieve whether ticking this physics engine will update the position of entities or if it will only change the calculated velocity.
     * @return if this engine will update the position of entities.
     */
    public boolean shouldUpdatePosition() {
        return this.updatePosition;
    }

    /**
     * Change whether ticking this physics engine will update the position of entities or if it will only change the calculated velocity.
     * @param enabled if this engine will update the position of entities.
     */
    public void setPositionUpdate(boolean enabled) {
        this.updatePosition = enabled;
    }

    public void tick() {
        if (this.getVelocity().getLength() > 0) {
            Vector3 newVelocity = this.getVelocity();
            if (!this.entity.isOnGround()) {
                newVelocity = newVelocity.add(this.airAcceleration);
            }

            newVelocity.setY(Math.max(Math.min(newVelocity.getY(), this.terminalYVelocity), -this.terminalYVelocity));

            if (this.shouldUpdatePosition()) {
                Location newLocation = this.entity.getLocation().add(newVelocity.getX(), newVelocity.getY(), newVelocity.getZ());

                // Check if an entity is intercepting any blocks at said new location
                BoundingBox newLocationBoundBox = new BoundingBox();
                newLocationBoundBox.setPosition(newLocation);
                newLocationBoundBox.setHeight(this.entity.getBoundingBox().getHeight());
                newLocationBoundBox.setWidth(this.entity.getBoundingBox().getWidth());

                int minBlockXCheck = (int) Math.floor(newLocation.getX() - newLocationBoundBox.getWidth() / 2);
                int minBlockYCheck = (int) Math.floor(newLocation.getY());
                int minBlockZCheck = (int) Math.floor(newLocation.getZ() - newLocationBoundBox.getWidth() / 2);

                int maxBlockXCheck = (int) Math.ceil(newLocation.getX() + newLocationBoundBox.getWidth() / 2);
                int maxBlockYCheck = (int) Math.ceil(newLocation.getY() + newLocationBoundBox.getHeight());
                int maxBlockZCheck = (int) Math.ceil(newLocation.getZ() + newLocationBoundBox.getWidth() / 2);

                for (int y = minBlockYCheck; y <= maxBlockYCheck; y++) {
                    for (int x = minBlockXCheck; x <= maxBlockXCheck; x++) {
                        for (int z = minBlockZCheck; z <= maxBlockZCheck; z++) {
                            Block block = this.entity.getWorld().getBlock(x, y, z);
                            if (block.getBoundingBox().collidesWith(newLocationBoundBox) && block.isSolid()) {
                                // Adjust x to no longer collide
                                if (newLocationBoundBox.collidesWithXAxis(block.getBoundingBox())) {
                                    newLocation.setX(this.entity.getX());
                                    newVelocity.setX(0);
                                }
                                newLocationBoundBox.setPosition(newLocation);

                                // Adjust z to no longer collide
                                if (newLocationBoundBox.collidesWithZAxis(block.getBoundingBox()) && newLocationBoundBox.collidesWith(block.getBoundingBox())) {
                                    newLocation.setZ(this.entity.getZ());
                                    newVelocity.setZ(0);
                                }
                                newLocationBoundBox.setPosition(newLocation);

                                // Adjust y to no longer collide
                                if (newLocationBoundBox.collidesWith(block.getBoundingBox()) && newLocationBoundBox.collidesWithYAxis(block.getBoundingBox())) {
                                    if (newLocation.getY() + newLocationBoundBox.getHeight() > block.getY()) {
                                        // our new location is in the ground
                                        newLocation.setY(block.getBoundingBox().getPosition().getY() + block.getBoundingBox().getHeight());
                                    } else {
                                        // our new location is too high (hitting a block)
                                        newLocation.setY(block.getY() - this.entity.getEyeHeight());
                                    }
                                    newVelocity.setY(0);
                                }
                                newLocationBoundBox.setPosition(newLocation);

                                if (newLocationBoundBox.collidesWith(block.getBoundingBox())) {
                                    // Entity is colliding with something no matter what.
                                    this.setVelocity(new Vector3(0, 0, 0));
                                    return;
                                }

                                break;
                            }
                        }
                    }
                }

                this.entity.moveTo(newLocation.getX(), newLocation.getY(), newLocation.getZ());
            }

            this.setVelocity(newVelocity);
        } else if (!this.entity.isOnGround()) {
            this.setVelocity(this.airAcceleration);
        }
    }

}
