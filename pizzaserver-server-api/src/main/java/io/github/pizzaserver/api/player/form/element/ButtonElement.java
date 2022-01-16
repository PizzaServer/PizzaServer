package io.github.pizzaserver.api.player.form.element;

import io.github.pizzaserver.api.player.form.FormImage;

public class ButtonElement {

    private final String text;
    private final FormImage image;


    public ButtonElement(String text, FormImage image) {
        this.text = text;
        this.image = image;
    }

    public ButtonElement(String text) {
        this.text = text;
        this.image = null;
    }

    public String getText() {
        return this.text;
    }

    public FormImage getImage() {
        return this.image;
    }
}
