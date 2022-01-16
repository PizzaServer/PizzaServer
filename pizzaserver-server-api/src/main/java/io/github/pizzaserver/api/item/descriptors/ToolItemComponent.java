package io.github.pizzaserver.api.item.descriptors;

import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public interface ToolItemComponent {

    ToolType getToolType();

    ToolTier getToolTier();
}
