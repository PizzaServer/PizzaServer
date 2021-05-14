package io.github.willqi.pizzaserver.player.data.skin;

public class SkinPersonaPieceTint {

    private final String id;
    private final String[] colors;

    public SkinPersonaPieceTint(String id, String[] colors) {
        this.id = id;
        this.colors = colors;
    }

    public String getId() {
        return this.id;
    }

    public String[] getColors() {
        return this.colors;
    }

}
