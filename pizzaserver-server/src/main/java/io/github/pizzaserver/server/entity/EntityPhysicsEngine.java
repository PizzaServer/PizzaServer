package io.github.pizzaserver.server.entity;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityPhysicsComponent;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.utils.BoundingBox;

import java.util.HashSet;
import java.util.Set;

public class EntityPhysicsEngine {

    private static final float MIN_HORIZONTAL_MOVEMENT_ALLOWED = 0.0001f;

    protected final ImplEntity entity;
    protected boolean updatePosition = true;

    protected Vector3f motion = Vector3f.ZERO;


    public EntityPhysicsEngine(ImplEntity entity) {
        this.entity = entity;
    }

    public Vector3f getMotion() {
        return Vector3f.from(this.motion.getX(), this.motion.getY(), this.motion.getZ());
    }

    public void setMotion(Vector3f motion) {
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
            this.handleCollisionWithEntities();
        }
        this.handleVelocity();
    }

    /**
     * Handles pushing other entities away from this entity if their bounding boxes intersect.
     */
    private void handleCollisionWithEntities() {
        BoundingBox entityBoundingBox = this.entity.getBoundingBox();
        int minChunkX = (int) Math.floor(entityBoundingBox.getMinX() / 16f);
        int minChunkZ = (int) Math.floor(entityBoundingBox.getMinZ() / 16f);
        int maxChunkX = (int) Math.ceil(entityBoundingBox.getMaxX() / 16f);
        int maxChunkZ = (int) Math.ceil(entityBoundingBox.getMaxZ() / 16f);

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
        if (this.getMotion().length() > 0) {
            this.applyFrictionToMotion();
            Vector3f newVelocity = this.getMotion();

            // Stop horizontal movement if it's small enough
            if (Math.abs(newVelocity.getX()) < MIN_HORIZONTAL_MOVEMENT_ALLOWED) {
                newVelocity = Vector3f.from(0, newVelocity.getY(), newVelocity.getZ());
            }
            if (Math.abs(newVelocity.getZ()) < MIN_HORIZONTAL_MOVEMENT_ALLOWED) {
                newVelocity = Vector3f.from(newVelocity.getX(), newVelocity.getY(), 0);
            }

            // Handle bounding box logic if we should update the position
            if (this.shouldUpdatePosition()) {
                float deltaX = newVelocity.getX();
                float deltaY = newVelocity.getY();
                float deltaZ = newVelocity.getZ();

                // Find all the blocks the entity is colliding with at the new location
                Set<Block> collidingBlocks = new HashSet<>();

                BoundingBox targetNewLocationBoundingBox = this.entity.getBoundingBox().translate(newVelocity);
                BoundingBox intersectingBlockBoundingBox = targetNewLocationBoundingBox.grow(0.5f);
                int minBlockXCheck = (int) Math.floor(intersectingBlockBoundingBox.getMinX());
                int maxBlockXCheck = (int) Math.ceil(intersectingBlockBoundingBox.getMaxX());
                int minBlockYCheck = (int) Math.floor(intersectingBlockBoundingBox.getMinY());
                int maxBlockYCheck = (int) Math.ceil(intersectingBlockBoundingBox.getMaxY());
                int minBlockZCheck = (int) Math.floor(intersectingBlockBoundingBox.getMinZ());
                int maxBlockZCheck = (int) Math.ceil(intersectingBlockBoundingBox.getMaxZ());

                for (int y = minBlockYCheck; y <= maxBlockYCheck; y++) {
                    for (int x = minBlockXCheck; x <= maxBlockXCheck; x++) {
                        for (int z = minBlockZCheck; z <= maxBlockZCheck; z++) {
                            Block block = this.entity.getWorld().getBlock(x, y, z);
                            if (block.getBlockState().hasCollision() && block.getBoundingBox().collidesWith(targetNewLocationBoundingBox)) {
                                collidingBlocks.add(block);
                            }
                        }
                    }
                }

                // Adjust velocity deltas to not collide
                BoundingBox newEntityBoundingBox = this.entity.getBoundingBox();

                // Adjust y
                for (Block block : collidingBlocks) {
                    deltaY = newEntityBoundingBox.calcYOffset(block.getBoundingBox(), deltaY);
                }
                newEntityBoundingBox = newEntityBoundingBox.translate(0, deltaY, 0);

                // Adjust x
                for (Block block : collidingBlocks) {
                    deltaX = newEntityBoundingBox.calcXOffset(block.getBoundingBox(), deltaX);
                }
                newEntityBoundingBox = newEntityBoundingBox.translate(deltaX, 0, 0);

                // Adjust z
                for (Block block : collidingBlocks) {
                    deltaZ = newEntityBoundingBox.calcZOffset(block.getBoundingBox(), deltaZ);
                }


                newVelocity = Vector3f.from(deltaX, deltaY, deltaZ);
                this.entity.moveTo(this.entity.getX() + deltaX, this.entity.getY() + deltaY, this.entity.getZ() + deltaZ);
            }
            this.setMotion(newVelocity);
        } else if (this.entity.hasGravity() && !this.entity.isOnGround()) {
            this.setMotion(0, -this.entity.getComponent(EntityPhysicsComponent.class).getGravityForce(), 0);
        }
    }

    private void applyFrictionToMotion() {
        EntityPhysicsComponent physicsComponent = this.entity.getComponent(EntityPhysicsComponent.class);
        Vector3f newMotion = this.getMotion();

        if (newMotion.length() > 0) {
            float friction = 1 - this.entity.getComponent(EntityPhysicsComponent.class).getDragForce();
            float newY;
            if (physicsComponent.applyDragBeforeGravity()) {
                newY = (newMotion.getY() * friction) - physicsComponent.getGravityForce();
            } else {
                newY = (newMotion.getY() - physicsComponent.getGravityForce()) * friction;
            }
            newMotion = Vector3f.from(newMotion.getX(), newY, newMotion.getZ());

            if (this.entity.isOnGround()) {
                // Consider block friction
                if (Math.abs(this.getMotion().getX()) > 0 || Math.abs(this.getMotion().getZ()) > 0) {
                    friction *= this.entity.getWorld().getBlock(this.entity.getLocation().toVector3i().sub(0, 1, 0)).getBlockState().getFriction();
                }
            }
            newMotion = newMotion.mul(friction, 1, friction);

            this.setMotion(newMotion);
        }
    }

}
