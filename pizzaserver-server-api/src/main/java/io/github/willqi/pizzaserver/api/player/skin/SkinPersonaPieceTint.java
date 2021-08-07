package io.github.willqi.pizzaserver.api.player.skin;

import java.util.List;

public class SkinPersonaPieceTint {

    private final String id;
    private final List<String> colors;

    public SkinPersonaPieceTint(String id, List<String> colors) {
        this.id = id;
        this.colors = colors;
    }

    public String getId() {
        return this.id;
    }

    public List<String> getColors() {
        return this.colors;
    }

}
