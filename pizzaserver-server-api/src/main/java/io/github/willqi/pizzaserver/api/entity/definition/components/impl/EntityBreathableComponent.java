package io.github.willqi.pizzaserver.api.entity.definition.components.impl;

import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class EntityBreathableComponent extends EntityComponent {

    private final Properties properties;


    public EntityBreathableComponent(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String getName() {
        return "minecraft:breathable";
    }

    public boolean canBreathSolids() {
        return this.properties.canBreathSolids();
    }

    /**
     * Retrieve all extra blocks this entity can breathe in regardless of canBreathSolids().
     * @return all extra blocks this entity can breathe in
     */
    public Set<BlockType> getBreathableBlocks() {
        return Collections.unmodifiableSet(this.properties.getBreathableBlocks());
    }

    /**
     * Retrieve all extra blocks this entity cannot breathe in regardless of canBreathSolids().
     * @return all extra blocks this entity cannot breathe in
     */
    public Set<BlockType> getNonBreathableBlocks() {
        return Collections.unmodifiableSet(this.properties.getNonBreathableBlocks());
    }

    public boolean generateBubblesInWater() {
        return this.properties.generateBubblesInWater();
    }

    /**
     * Amount of seconds it takes to recover all of your air supply from 0.
     * @return amount of seconds
     */
    public float getInhaleTime() {
        return this.properties.getInhaleTime();
    }

    /**
     * Amount of seconds your air supply lasts.
     * @return amount of seconds
     */
    public float getTotalSupplyTime() {
        return this.properties.getTotalSupplyTime();
    }

    /**
     * How often you will take suffocation damage in ticks.
     * @return ticks
     */
    public int getSuffocationInterval() {
        return this.properties.getSuffocationInterval();
    }


    public static class Properties {

        private boolean canBreathSolids;
        private Set<BlockType> breathableBlocks = new HashSet<>();
        private Set<BlockType> nonBreathableBlocks = new HashSet<>();
        private boolean generateBubblesInWater;
        private float inhaleTime;
        private float totalSupply;
        private int suffocationInterval;


        public boolean canBreathSolids() {
            return this.canBreathSolids;
        }

        public Properties setCanBreathSolids(boolean enabled) {
            this.canBreathSolids = enabled;
            return this;
        }

        /**
         * Retrieve all extra blocks this entity can breathe in regardless of canBreathSolids().
         * @return all extra blocks this entity can breathe in
         */
        public Set<BlockType> getBreathableBlocks() {
            return this.breathableBlocks;
        }

        /**
         * Set extra blocks that this entity can breathe in regardless of canBreathSolids().
         * @param breathableBlocks extra blocks this entity can breathe in
         */
        public Properties setBreathableBlocks(Set<BlockType> breathableBlocks) {
            this.breathableBlocks = breathableBlocks;
            return this;
        }

        /**
         * Retrieve all extra blocks this entity cannot breathe in regardless of canBreathSolids().
         * @return all extra blocks this entity cannot breathe in
         */
        public Set<BlockType> getNonBreathableBlocks() {
            return this.nonBreathableBlocks;
        }

        /**
         * Set extra blocks that this entity cannot breathe in regardless of canBreathSolids().
         * @param nonBreathableBlocks all extra blocks this entity cannot breathe in
         */
        public Properties setNonBreathableBlocks(Set<BlockType> nonBreathableBlocks) {
            this.nonBreathableBlocks = nonBreathableBlocks;
            return this;
        }

        public boolean generateBubblesInWater() {
            return this.generateBubblesInWater;
        }

        public Properties setGenerateBubblesInWater(boolean enabled) {
            this.generateBubblesInWater = enabled;
            return this;
        }

        /**
         * Get the amount of seconds it takes to recover all of your oxygen from 0.
         * @return amount of seconds
         */
        public float getInhaleTime() {
            return this.inhaleTime;
        }

        /**
         * Modify the amount of seconds it takes to recover all of your oxygen from 0.
         */
        public Properties setInhaleTime(float inhaleTime) {
            this.inhaleTime = inhaleTime;
            return this;
        }

        /**
         * Amount of seconds this entity's air supply lasts.
         * @return amount of seconds
         */
        public float getTotalSupplyTime() {
            return this.totalSupply;
        }

        /**
         * Modify the amount of seconds this entity's air supply lasts.
         */
        public Properties setTotalSupplyTime(int totalSupply) {
            this.totalSupply = totalSupply;
            return this;
        }

        public int getSuffocationInterval() {
            return this.suffocationInterval;
        }

        public Properties setSuffocationInterval(int interval) {
            this.suffocationInterval = interval;
            return this;
        }

    }

}
