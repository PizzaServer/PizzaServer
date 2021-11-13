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

    public boolean canBreathAir() {
        return this.properties.canBreathAir();
    }

    public boolean canBreathWater() {
        return this.properties.canBreathWater();
    }

    public boolean canBreathLava() {
        return this.properties.canBreathLava();
    }

    public boolean canBreathSolids() {
        return this.properties.canBreathSolids();
    }

    public Set<BlockType> getBreathableBlocks() {
        return Collections.unmodifiableSet(this.properties.getBreathableBlocks());
    }

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
    public int getInhaleTime() {
        return this.properties.getInhaleTime();
    }

    /**
     * Amount of seconds your air supply lasts.
     * @return amount of seconds
     */
    public int getTotalSupplyTime() {
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

        private boolean canBreathAir;
        private boolean canBreathWater;
        private boolean canBreathLava;
        private boolean canBreathSolids;
        private Set<BlockType> breathableBlocks = new HashSet<>();
        private Set<BlockType> nonBreathableBlocks = new HashSet<>();
        private boolean generateBubblesInWater;
        private int inhaleTime;
        private int totalSupply;
        private int suffocationInterval;

        public boolean canBreathAir() {
            return this.canBreathAir;
        }

        public Properties setCanBreathAir(boolean enabled) {
            this.canBreathAir = enabled;
            return this;
        }

        public boolean canBreathWater() {
            return this.canBreathWater;
        }

        public Properties setCanBreathWater(boolean enabled) {
            this.canBreathWater = enabled;
            return this;
        }

        public boolean canBreathLava() {
            return this.canBreathLava;
        }

        public Properties setCanBreathLava(boolean enabled) {
            this.canBreathLava = enabled;
            return this;
        }

        public boolean canBreathSolids() {
            return this.canBreathSolids;
        }

        public Properties setCanBreathSolids(boolean enabled) {
            this.canBreathSolids = enabled;
            return this;
        }

        public Set<BlockType> getBreathableBlocks() {
            return this.breathableBlocks;
        }

        public Properties setBreathableBlocks(Set<BlockType> breathableBlocks) {
            this.breathableBlocks = breathableBlocks;
            return this;
        }

        public Set<BlockType> getNonBreathableBlocks() {
            return this.nonBreathableBlocks;
        }

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
        public int getInhaleTime() {
            return this.inhaleTime;
        }

        /**
         * Modify the amount of seconds it takes to recover all of your oxygen from 0.
         */
        public Properties setInhaleTime(int inhaleTime) {
            this.inhaleTime = inhaleTime;
            return this;
        }

        /**
         * Amount of seconds your air supply lasts.
         * @return amount of seconds
         */
        public int getTotalSupplyTime() {
            return this.totalSupply;
        }

        /**
         * Modify the amount of seconds your air supply lasts.
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
