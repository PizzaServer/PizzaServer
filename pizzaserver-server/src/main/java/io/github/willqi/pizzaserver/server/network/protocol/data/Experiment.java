package io.github.willqi.pizzaserver.server.network.protocol.data;

/**
 * Represents an experiment in a Minecraft world
 */
public enum Experiment {

    DATA_DRIVEN_ITEMS("data_driven_items");


    private final String id;


    Experiment(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

}
