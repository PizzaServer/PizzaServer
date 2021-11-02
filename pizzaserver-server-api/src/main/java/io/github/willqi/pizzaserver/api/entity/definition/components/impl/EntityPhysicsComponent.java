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

    public static class Properties {

        private boolean collision;
        private boolean gravity;

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

    }

}
