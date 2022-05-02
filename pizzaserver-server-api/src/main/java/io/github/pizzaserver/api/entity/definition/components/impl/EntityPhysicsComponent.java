package io.github.pizzaserver.api.entity.definition.components.impl;

import io.github.pizzaserver.api.entity.definition.components.EntityComponent;

/**
 * This entity component describes if this entity is affected by collision/gravity.
 */
public class EntityPhysicsComponent extends EntityComponent {

    private final Properties properties;


    public EntityPhysicsComponent(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String getName() {
        return "minecraft:physics";
    }

    /**
     * If this entity should collide with other entities.
     * @return if this entity should collide with other entities.
     */
    public boolean hasCollision() {
        return this.properties.hasCollision();
    }

    public boolean hasGravity() {
        return this.properties.hasGravity();
    }

    public float getGravity() {
        return this.properties.getGravity();
    }

    public float getDrag() {
        return this.properties.getDrag();
    }

    public boolean applyDragBeforeGravity() {
        return this.properties.applyDragBeforeGravity();
    }

    public boolean isPushable() {
        return this.properties.isPushable();
    }

    public boolean isPistonPushable() {
        return this.properties.isPistonPushable();
    }


    public static class Properties {

        private boolean collision;
        private boolean hasGravity;
        private boolean pushable;
        private boolean pistonPushable;

        private float gravity;
        private float drag;
        private boolean applyDragBeforeGravity;

        /**
         * If this entity should collide with other entities.
         * @return if this entity should collide with other entities.
         */
        public boolean hasCollision() {
            return this.collision;
        }

        /**
         * Change if this entity should collide with other entities.
         */
        public Properties setCollision(boolean collision) {
            this.collision = collision;
            return this;
        }

        public boolean hasGravity() {
            return this.hasGravity;
        }

        /**
         * Sets if gravity is applied to this entity.
         */
        public Properties setHasGravity(boolean hasGravity) {
            this.hasGravity = hasGravity;
            return this;
        }

        public float getGravity() {
            return this.gravity;
        }

        /**
         * Sets gravity in blocks per tick per tick (blocks/tick²).
         */
        public Properties setGravity(float gravity) {
            this.gravity = gravity;
            return this;
        }

        public float getDrag() {
            return this.drag;
        }

        /**
         * Sets entity drag. Valid values range from 0.0 (no drag) to 1.0 (max drag).
         * A drag of 0.2 will cause the entity to lose 20% of its velocity each tick.
         */
        public Properties setDrag(float drag) {
            this.drag = drag;
            return this;
        }

        public boolean applyDragBeforeGravity() {
            return this.applyDragBeforeGravity;
        }

        public Properties setApplyDragBeforeGravity(boolean applyDragBeforeGravity) {
            this.applyDragBeforeGravity = applyDragBeforeGravity;
            return this;
        }

        public boolean isPushable() {
            return this.pushable;
        }

        public Properties setPushable(boolean pushable) {
            this.pushable = pushable;
            return this;
        }

        public boolean isPistonPushable() {
            return this.pistonPushable;
        }

        public Properties setPistonPushable(boolean pistonPushable) {
            this.pistonPushable = pistonPushable;
            return this;
        }

    }

}
