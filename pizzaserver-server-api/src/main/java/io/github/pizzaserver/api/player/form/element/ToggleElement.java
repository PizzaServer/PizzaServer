package io.github.pizzaserver.api.player.form.element;

import com.google.gson.annotations.SerializedName;

public class ToggleElement extends CustomElement {

    @SerializedName("default")
    private final boolean enabled;

    public ToggleElement(String text, boolean enabled) {
        super(ElementType.TOGGLE, text);
        this.enabled = enabled;
    }

    public ToggleElement(String text) {
        this(text, false);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

}
