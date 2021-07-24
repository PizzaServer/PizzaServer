package io.github.willqi.pizzaserver.server.player.skin;

import io.github.willqi.pizzaserver.api.player.skin.SkinPersonaPieceTint;

import java.util.List;

public class BedrockSkinPersonaPieceTint implements SkinPersonaPieceTint {

    private final String id;
    private final List<String> colors;

    public BedrockSkinPersonaPieceTint(String id, List<String> colors) {
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
