package io.github.willqi.pizzaserver.server.player.skin;

import io.github.willqi.pizzaserver.api.player.skin.APISkinPersonaPieceTint;

import java.util.List;

public class SkinPersonaPieceTint implements APISkinPersonaPieceTint {

    private final String id;
    private final List<String> colors;

    public SkinPersonaPieceTint(String id, List<String> colors) {
        this.id = id;
        this.colors = colors;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public List<String> getColors() {
        return this.colors;
    }

}
