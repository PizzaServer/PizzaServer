package io.github.pizzaserver.api.level.world.data;

import io.github.pizzaserver.commons.utils.Check;

public enum Dimension {
    OVERWORLD("overworld", -64, 319, 1),
    NETHER("nether", 0, 127, 3),
    END("end", 0, 255, 4);

    private final String id;
    private final int minHeight;
    private final int maxHeight;
    private final int generator;

    Dimension(String id, int minHeight, int maxHeight, int generator) {


        this.id = id;
        this.minHeight = Check.withinUpperBoundInclusive(minHeight, maxHeight, "minHeight");
        this.maxHeight = Check.withinLowerBoundInclusive(maxHeight, minHeight, "maxHeight");
        this.generator = generator;
    }

    public String getId() {
        return this.id;
    }

    public int getMinHeight() {
        return this.minHeight;
    }

    public int getMaxHeight() {
        return this.maxHeight;
    }

    public int getHeight() {
        return this.getMaxHeight() - this.getMinHeight() + 1;
    }

    public int getGenerator() {
        return this.generator;
    }

}
