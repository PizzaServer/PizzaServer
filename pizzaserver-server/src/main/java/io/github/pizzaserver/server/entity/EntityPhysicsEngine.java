package io.github.pizzaserver.server.entity;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityPhysicsComponent;
import io.github.pizzaserver.api.level.world.blocks.Block;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.utils.BoundingBox;

public class EntityPhysicsEngine {

    private static final float MIN_HORIZONTAL_MOVEMENT_ALLOWED = 0.0001f;

    protected final ImplEntity entity;
    protected boolean updatePosition = true;

    protected Vector3f motion = Vector3f.ZERO;
    protected Vector3f lastMotion = Vector3f.ZERO;


    public EntityPhysicsEngine(ImplEntity entity) {
        this.entity = entity;
    }

    public Vector3f getMotion() {
        return Vector3f.from(this.motion.getX(), this.motion.getY(), this.motion.getZ());
    }

    public void setMotion(Vector3f motion) {
        this.lastMotion = this.motion;
        this.motion = motion;
    }

    public void setMotion(float x, float y, float z) {
        this.setMotion(Vector3f.from(x, y, z));
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
            this.setMotion(0, 0, 0);
            return;
        }
        if (this.entity.hasCollision()) {
            this.handleCollision();
        }
        this.handleVelocity();
    }

    /**
     * Handles pushing other entities away from this entity if their bounding boxes intersect.
     */
    private void handleCollision() {
        int minChunkX = (int) Math.floor((this.entity.getFloorX() - this.entity.getBoundingBox().getWidth() / 2) / 16f);
        int minChunkZ = (int) Math.floor((this.entity.getFloorZ() - this.entity.getBoundingBox().getWidth() / 2) / 16f);
        int maxChunkX = (int) Math.ceil((this.entity.getFloorX() + this.entity.getBoundingBox().getWidth() / 2) / 16f);
        int maxChunkZ = (int) Math.ceil((this.entity.getFloorZ() + this.entity.getBoundingBox().getWidth() / 2) / 16f);

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                Chunk chunk = this.entity.getWorld().getChunk(chunkX, chunkZ);
                for (Entity entity : chunk.getEntities()) {
                    boolean canPushEntity = !entity.equals(this.entity)
                            && entity.getBoundingBox().collidesWith(this.entity.getBoundingBox())
                            && entity.isPushable();
                    if (canPushEntity) {
                        float xDiff = entity.getX() - this.entity.getX();
                        float zDiff = entity.getZ() - this.entity.getZ();
                        if (xDiff != 0 && zDiff != 0) {
                            float xUnit = xDiff * (1 / Math.abs(xDiff));
                            float zUnit = zDiff * (1 / Math.abs(zDiff));
                            entity.setMotion(entity.getMotion().add(entity.getMovementSpeed() * xUnit, 0, entity.getMovementSpeed() * zUnit));
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles applying velocity logic.
     */
    private void handleVelocity() {
        EntityPhysicsComponent physicsComponent = this.entity.getComponent(EntityPhysicsComponent.class);
        if (this.getMotion().length() > 0) {
            Vector3f newVelocity = this.getMotion();

            float friction = 1 - this.entity.getComponent(EntityPhysicsComponent.class).getDragForce();
            float newY;
            if (physicsComponent.applyDragBeforeGravity()) {
                newY = (newVelocity.getY() * friction) - physicsComponent.getGravityForce();
            } else {
                newY = (newVelocity.getY() - physicsComponent.getGravityForce()) * friction;
            }
            newVelocity = Vector3f.from(newVelocity.getX(), newY, newVelocity.getZ());

            // Apply friction
            if (this.entity.isOnGround()) {
                // Consider block friction
                if (Math.abs(this.getMotion().getX()) > 0 || Math.abs(this.getMotion().getZ()) > 0) {
                    friction *= this.entity.getWorld().getBlock(this.entity.getLocation().toVector3i().sub(0, 1, 0)).getBlockState().getFriction();
                }
            }
            newVelocity = newVelocity.mul(friction, 1, friction);

            // Stop horizontal movement if it's small enough
            if (Math.abs(newVelocity.getX()) < MIN_HORIZONTAL_MOVEMENT_ALLOWED) {
                newVelocity = Vector3f.from(0, newVelocity.getY(), newVelocity.getZ());
            }
            if (Math.abs(newVelocity.getZ()) < MIN_HORIZONTAL_MOVEMENT_ALLOWED) {
                newVelocity = Vector3f.from(newVelocity.getX(), newVelocity.getY(), 0);
            }

            // Handle bounding box logic if we should update the position
            if (this.shouldUpdatePosition()) {
                Vector3f newPosition = this.entity.getLocation().toVector3f().add(newVelocity.getX(), newVelocity.getY(), newVelocity.getZ());

                // Check if an entity is intercepting any blocks at said new location
                BoundingBox newLocationBoundBox = new BoundingBox();
                newLocationBoundBox.setPosition(newPosition);
                newLocationBoundBox.setHeight(this.entity.getBoundingBox().getHeight());
                newLocationBoundBox.setWidth(this.entity.getBoundingBox().getWidth());

                int minBlockXCheck = (int) Math.floor(newPosition.getX() - newLocationBoundBox.getWidth() / 2);
                int maxBlockXCheck = (int) Math.ceil(newPosition.getX() + newLocationBoundBox.getWidth() / 2);
                int minBlockYCheck = (int) Math.floor(newPosition.getY());
                int maxBlockYCheck = (int) Math.ceil(newPosition.getY() + newLocationBoundBox.getHeight());
                int maxBlockZCheck = (int) Math.ceil(newPosition.getZ() + newLocationBoundBox.getWidth() / 2);
                int minBlockZCheck = (int) Math.floor(newPosition.getZ() - newLocationBoundBox.getWidth() / 2);

                for (int y = minBlockYCheck; y <= maxBlockYCheck; y++) {
                    for (int x = minBlockXCheck; x <= maxBlockXCheck; x++) {
                        for (int z = minBlockZCheck; z <= maxBlockZCheck; z++) {
                            Block block = this.entity.getWorld().getBlock(x, y, z);

                            if (block.getBoundingBox().collidesWith(newLocationBoundBox) && block.getBlockState().isSolid()) {
                                // ensure we are not stuck in the ground before checking x and z (otherwise no friction will ever be applied)
                                if (block.getBoundingBox().collidesWithYAxis(newLocationBoundBox) && newPosition.getY() > block.getY() + block.getBoundingBox().getHeight() / 2) {
                                    newPosition = Vector3f.from(newPosition.getX(), block.getBoundingBox().getPosition().getY() + block.getBoundingBox().getHeight(), newPosition.getZ());
                                    newVelocity = Vector3f.from(newVelocity.getX(), 0, newVelocity.getZ());
                                    newLocationBoundBox.setPosition(newPosition);
                                }

                                // Adjust x to no longer collide
                                if (newLocationBoundBox.collidesWithXAxis(block.getBoundingBox()) && newLocationBoundBox.collidesWith(block.getBoundingBox())) {
                                    newPosition = Vector3f.from(this.entity.getX(), newPosition.getY(), newPosition.getZ());
                                    newVelocity = Vector3f.from(0, newVelocity.getY(), newVelocity.getZ());
                                    newLocationBoundBox.setPosition(newPosition);
                                }

                                // Adjust z to no longer collide
                                if (newLocationBoundBox.collidesWithZAxis(block.getBoundingBox()) && newLocationBoundBox.collidesWith(block.getBoundingBox())) {
                                    newPosition = Vector3f.from(newPosition.getX(), newPosition.getY(), this.entity.getZ());
                                    newVelocity = Vector3f.from(newVelocity.getX(), newVelocity.getY(), 0);
                                    newLocationBoundBox.setPosition(newPosition);
                                }

                                // The only possible way for this to still be a collision in y is if we're in the bottom half of a block.
                                if (newLocationBoundBox.collidesWith(block.getBoundingBox()) && newLocationBoundBox.collidesWithYAxis(block.getBoundingBox())) {
                                    newPosition = Vector3f.from(newPosition.getX(), block.getY() - this.entity.getEyeHeight(), newPosition.getZ());
                                    newVelocity = Vector3f.from(newVelocity.getX(), 0, newVelocity.getZ());
                                    newLocationBoundBox.setPosition(newPosition);
                                }

                                if (newLocationBoundBox.collidesWith(block.getBoundingBox())) {
                                    // Entity is colliding with something no matter what.
                                    newVelocity = Vector3f.ZERO;
                                    newPosition = this.entity.getLocation().toVector3f();
                                }

                                break;
                            }
                        }
                    }
                }

                this.entity.moveTo(newPosition.getX(), newPosition.getY(), newPosition.getZ());
            }
            this.setMotion(newVelocity);
        } else if (this.entity.hasGravity() && !this.entity.isOnGround()) {
            this.setMotion(0, -this.entity.getComponent(EntityPhysicsComponent.class).getGravityForce(), 0);
        }
    }

}
