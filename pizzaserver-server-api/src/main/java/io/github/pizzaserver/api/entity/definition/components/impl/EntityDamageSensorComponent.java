package io.github.pizzaserver.api.entity.definition.components.impl;

import io.github.pizzaserver.api.entity.data.DamageCause;
import io.github.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.pizzaserver.api.entity.definition.components.filter.EntityFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Entity component that defines what damage an entity can take.
 */
public class EntityDamageSensorComponent extends EntityComponent {

    private final Sensor[] sensors;


    public EntityDamageSensorComponent(Sensor[] sensors) {
        this.sensors = sensors;
    }

    public Sensor[] getSensors() {
        return this.sensors;
    }

    @Override
    public String getName() {
        return "minecraft:damage_sensor";
    }


    public static class Sensor {

        private DamageCause cause = null;

        private float damageModifier = 1;
        private float damageMultiplier = 1;
        private boolean dealsDamage = true;

        private final List<EntityFilter> filters = new ArrayList<>();

        private String sound = null;


        /**
         * Set the cause that this sensor will listen for.
         *
         * @param cause damage cause
         */
        public Sensor setCause(DamageCause cause) {
            this.cause = cause;
            return this;
        }

        /**
         * Get the cause that this sensor is listening for.
         *
         * @return damage cause
         */
        public Optional<DamageCause> getCause() {
            return Optional.ofNullable(this.cause);
        }

        /**
         * Set the amount of damage to add to the total damage.
         *
         * @param modifier amount of damage to add
         */
        public Sensor setDamageModifier(float modifier) {
            this.damageModifier = modifier;
            return this;
        }

        /**
         * Get the amount of damage to be added to the total damage.
         *
         * @return amount of damage to add
         */
        public float getDamageModifier() {
            return this.damageModifier;
        }

        /**
         * Set the damage multiplier for the total damage.
         *
         * @param multiplier multiplier
         */
        public Sensor setDamageMultiplier(float multiplier) {
            this.damageMultiplier = multiplier;
            return this;
        }

        /**
         * Get the damage multiplier for the total damage.
         *
         * @return damage multiplier
         */
        public float getDamageMultiplier() {
            return this.damageMultiplier;
        }

        public Sensor setDealsDamage(boolean dealsDamage) {
            this.dealsDamage = dealsDamage;
            return this;
        }

        public boolean dealsDamage() {
            return this.dealsDamage;
        }

        /**
         * Set the sound to be sent when damage is dealt to the entity.
         *
         * @param sound sound
         */
        public Sensor setSound(String sound) {
            this.sound = sound;
            return this;
        }

        /**
         * Get the sound to be sent when damage is dealt to the entity.
         *
         * @return sound
         */
        public Optional<String> getSound() {
            return Optional.ofNullable(this.sound);
        }

        public Sensor addFilter(EntityFilter filter) {
            this.filters.add(filter);
            return this;
        }

        public List<EntityFilter> getFilters() {
            return Collections.unmodifiableList(this.filters);
        }
    }
}
