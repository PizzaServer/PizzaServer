package io.github.pizzaserver.api.item.descriptors;

import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public interface ToolItemComponent extends DurableItemComponent {

    ToolType getToolType();

    ToolTier getToolTier();

}
