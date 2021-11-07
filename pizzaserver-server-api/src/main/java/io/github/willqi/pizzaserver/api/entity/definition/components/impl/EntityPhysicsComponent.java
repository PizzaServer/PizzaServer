package io.github.willqi.pizzaserver.api.entity.definition.components.impl;

import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponent;

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

    public float getGravityForce() {
        return this.properties.getGravityForce();
    }

    public float getDragForce() {
        return this.properties.getDragForce();
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
        private boolean gravity;
        private boolean pushable;
        private boolean pistonPushable;

        private float gravityForce;
        private float dragForce;
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
            return this.gravity;
        }

        public Properties setGravity(boolean gravity) {
            this.gravity = gravity;
            return this;
        }

        public float getGravityForce() {
            return this.gravityForce;
        }

        public Properties setGravityForce(float gravityForce) {
            this.gravityForce = gravityForce;
            return this;
        }

        public float getDragForce() {
            return this.dragForce;
        }

        public Properties setDragForce(float dragForce) {
            this.dragForce = dragForce;
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
