package io.github.willqi.pizzaserver.api.entity.definition.filter;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;

import java.util.Optional;

public class EntityFilterData {

    private Entity self;
    private Entity other;
    private ItemStack itemStack;
    private Block block;


    private EntityFilterData(Entity self,
                             Entity other,
                             ItemStack itemStack,
                             Block block) {
        this.self = self;
        this.other = other;
        this.itemStack = itemStack;
        this.block = block;
    }


    public Entity getSelf() {
        return this.self;
    }

    public Optional<Entity> getOther() {
        return Optional.ofNullable(this.other);
    }

    public Optional<ItemStack> getItemStack() {
        return Optional.ofNullable(this.itemStack);
    }

    public Optional<Block> getBlock() {
        return Optional.ofNullable(this.block);
    }


    public static class Builder {

        private Entity self;
        private Entity other;
        private ItemStack itemStack;
        private Block block;


        public Builder setSelf(Entity self) {
            this.self = self;
            return this;
        }

        public Builder setOther(Entity other) {
            this.other = other;
            return this;
        }

        public Builder setItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            return this;
        }

        public Builder setBlock(Block block) {
            this.block = block;
            return this;
        }

        public EntityFilterData build() {
            return new EntityFilterData(this.self, this.other, this.itemStack, this.block);
        }

    }

}
