package io.github.pizzaserver.api.player.form.element;

/**
 * An element that can only be used on custom forms.
 */
public abstract class CustomElement {

    protected final ElementType type;
    protected final String text;


    protected CustomElement(ElementType type, String text) {
        this.type = type;
        this.text = text;
    }

    public ElementType getType() {
        return this.type;
    }

    public String getText() {
        return this.text;
    }
}
