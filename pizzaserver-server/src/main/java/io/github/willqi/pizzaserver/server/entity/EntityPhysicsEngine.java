package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.BoundingBox;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

public class EntityPhysicsEngine {

    private static final float MIN_HORIZONTAL_MOVEMENT_ALLOWED = 0.01f;

    protected final ImplEntity entity;
    protected boolean updatePosition = true;

    protected Vector3 velocity = new Vector3(0, 0, 0);
    protected Vector3 lastVelocity = new Vector3(0, 0, 0);
    protected float airAcceleration = -0.05f;
    protected float terminalYVelocity = 0.4f;


    public EntityPhysicsEngine(ImplEntity entity) {
        this.entity = entity;
    }

    public Vector3 getVelocity() {
        return new Vector3(this.velocity.getX(), this.velocity.getY(), this.velocity.getZ());
    }

    public void setVelocity(Vector3 velocity) {
        this.lastVelocity = this.velocity;
        this.velocity = velocity;
    }

    public void setVelocity(float x, float y, float z) {
        this.setVelocity(new Vector3(x, y, z));
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
        if (!this.entity.hasAI()) {
            this.setVelocity(0, 0, 0);
            return;
        }

        if (this.getVelocity().getLength() > 0) {
            Vector3 newVelocity = this.getVelocity();

            if (this.entity.isOnGround()) {
                if (this.lastVelocity.getLength() > 0 && (this.getVelocity().getX() != 0 || this.getVelocity().getZ() != 0)) {
                    float friction = 1 - this.entity.getWorld().getBlock(this.entity.getLocation().round().toVector3i().subtract(0, 1, 0)).getBlockType().getFriction();
                    newVelocity = newVelocity.subtract((newVelocity.getX() * friction), 0, (newVelocity.getZ() * friction));

                    if (Math.abs(newVelocity.getX()) < MIN_HORIZONTAL_MOVEMENT_ALLOWED) {
                        newVelocity.setX(0);
                    }
                    if (Math.abs(newVelocity.getZ()) < MIN_HORIZONTAL_MOVEMENT_ALLOWED) {
                        newVelocity.setZ(0);
                    }
                }
            } else {
                newVelocity = newVelocity.add(0, this.airAcceleration, 0);
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
                                // ensure we are not stuck in the ground before checking x and z (otherwise no friction will ever be applied)
                                if (block.getBoundingBox().collidesWithYAxis(newLocationBoundBox) && newLocation.getY() > block.getY() + block.getBoundingBox().getHeight() / 2) {
                                    newLocation.setY(block.getBoundingBox().getPosition().getY() + block.getBoundingBox().getHeight());
                                    newVelocity.setY(0);
                                    newLocationBoundBox.setPosition(newLocation);
                                }

                                // Adjust x to no longer collide
                                if (newLocationBoundBox.collidesWithXAxis(block.getBoundingBox()) && newLocationBoundBox.collidesWith(block.getBoundingBox())) {
                                    newLocation.setX(this.entity.getX());
                                    newVelocity.setX(0);
                                    newLocationBoundBox.setPosition(newLocation);
                                }

                                // Adjust z to no longer collide
                                if (newLocationBoundBox.collidesWithZAxis(block.getBoundingBox()) && newLocationBoundBox.collidesWith(block.getBoundingBox())) {
                                    newLocation.setZ(this.entity.getZ());
                                    newVelocity.setZ(0);
                                    newLocationBoundBox.setPosition(newLocation);
                                }

                                // The only possible way for this to still be a collision in y is if we're in the bottom half of a block.
                                if (newLocationBoundBox.collidesWith(block.getBoundingBox()) && newLocationBoundBox.collidesWithYAxis(block.getBoundingBox())) {
                                    newLocation.setY(block.getY() - this.entity.getEyeHeight());
                                    newVelocity.setY(0);
                                    newLocationBoundBox.setPosition(newLocation);
                                }

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
        } else if (this.entity.hasGravity() && !this.entity.isOnGround()) {
            this.setVelocity(0, this.airAcceleration, 0);
        }
    }

}
