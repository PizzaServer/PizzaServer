package io.github.pizzaserver.api.item.descriptors;

import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public interface ToolItem extends DurableItem {

    ToolType getToolType();

    ToolTier getToolTier();

}
