package io.github.pizzaserver.api.entity.definition.components.filter;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;

import java.util.Optional;

public class EntityFilterData {

    private final Entity self;
    private final Entity other;
    private final Item item;
    private final Block block;


    private EntityFilterData(Entity self,
                             Entity other,
                             Item item,
                             Block block) {
        this.self = self;
        this.other = other;
        this.item = item;
        this.block = block;
    }


    public Entity getSelf() {
        return this.self;
    }

    public Optional<Entity> getOther() {
        return Optional.ofNullable(this.other);
    }

    public Optional<Item> getItem() {
        return Optional.ofNullable(this.item);
    }

    public Optional<Block> getBlock() {
        return Optional.ofNullable(this.block);
    }


    public static class Builder {

        private Entity self;
        private Entity other;
        private Item item;
        private Block block;


        public Builder setSelf(Entity self) {
            this.self = self;
            return this;
        }

        public Builder setOther(Entity other) {
            this.other = other;
            return this;
        }

        public Builder setItem(Item item) {
            this.item = item;
            return this;
        }

        public Builder setBlock(Block block) {
            this.block = block;
            return this;
        }

        public EntityFilterData build() {
            return new EntityFilterData(this.self, this.other, this.item, this.block);
        }

    }

}
