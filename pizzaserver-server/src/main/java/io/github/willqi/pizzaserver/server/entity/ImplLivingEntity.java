package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.entity.attributes.Attribute;
import io.github.willqi.pizzaserver.api.entity.attributes.AttributeType;
import io.github.willqi.pizzaserver.api.entity.inventory.LivingEntityInventory;
import io.github.willqi.pizzaserver.api.entity.LivingEntity;
import io.github.willqi.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.willqi.pizzaserver.server.entity.inventory.ImplLivingEntityInventory;

import java.util.Collections;
import java.util.Set;

public class ImplLivingEntity extends ImplEntity implements LivingEntity {

    protected LivingEntityInventory inventory = null;


    public ImplLivingEntity(EntityDefinition entityDefinition) {
        super(entityDefinition);
        this.inventory = new ImplLivingEntityInventory(this, Collections.emptySet(), 36);   // TODO: Change when inventory component is implemented
    }

    @Override
    public float getHealth() {
        return this.getAttribute(AttributeType.HEALTH).getCurrentValue();
    }

    @Override
    public void setHealth(float health) {
        Attribute attribute = this.getAttribute(AttributeType.HEALTH);

        float newHealth = Math.max(attribute.getMinimumValue(), Math.min(health, this.getMaxHealth()));
        attribute.setCurrentValue(newHealth);

        if (this.getHealth() <= 0) {
            // TODO: kill
        }
    }

    @Override
    public float getMaxHealth() {
        return this.getAttribute(AttributeType.HEALTH).getMaximumValue();
    }

    @Override
    public void setMaxHealth(float maxHealth) {
        Attribute attribute = this.getAttribute(AttributeType.HEALTH);

        float newMaxHealth = Math.max(attribute.getMinimumValue(), maxHealth);
        attribute.setMaximumValue(newMaxHealth);
        attribute.setCurrentValue(Math.min(this.getHealth(), this.getMaxHealth()));
    }

    @Override
    public float getAbsorption() {
        return this.getAttribute(AttributeType.ABSORPTION).getCurrentValue();
    }

    @Override
    public void setAbsorption(float absorption) {
        Attribute attribute = this.getAttribute(AttributeType.ABSORPTION);

        float newAbsorption = Math.max(attribute.getMinimumValue(), Math.min(absorption, this.getMaxAbsorption()));
        attribute.setCurrentValue(newAbsorption);
    }

    @Override
    public float getMaxAbsorption() {
        return this.getAttribute(AttributeType.ABSORPTION).getMaximumValue();
    }

    @Override
    public void setMaxAbsorption(float maxAbsorption) {
        Attribute attribute = this.getAttribute(AttributeType.ABSORPTION);

        float newMaxAbsorption = Math.max(attribute.getMinimumValue(), maxAbsorption);
        attribute.setMaximumValue(newMaxAbsorption);
        attribute.setCurrentValue(Math.min(this.getAbsorption(), this.getMaxAbsorption()));
    }

    @Override
    public LivingEntityInventory getInventory() {
        return this.inventory;
    }

}
