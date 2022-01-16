package io.github.pizzaserver.api.player.form;

import io.github.pizzaserver.api.player.form.element.CustomElement;

import java.util.*;

/**
 * Custom forms are forms designed for player input.
 */
public class CustomForm extends Form {

    private final List<CustomElement> content;
    private final FormImage icon;

    protected CustomForm(String title, FormImage icon, List<CustomElement> elements) {
        super(FormType.CUSTOM, title);
        this.content = elements;
        this.icon = icon;
    }

    public List<CustomElement> getElements() {
        return Collections.unmodifiableList(this.content);
    }

    public CustomElement getElement(int elementIndex) {
        return this.content.get(elementIndex);
    }

    /**
     * Get the server setting icon if present.
     * @return icon if present
     */
    public Optional<FormImage> getIcon() {
        return Optional.ofNullable(this.icon);
    }


    public static class Builder {

        private String title = "";
        private FormImage icon;
        private final List<CustomElement> elements = new ArrayList<>();


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * This icon only exists for server setting forms.
         * @param icon icon of the form
         * @return icon
         */
        public Builder setIcon(FormImage icon) {
            this.icon = icon;
            return this;
        }

        public Builder addElement(CustomElement customElement) {
            this.elements.add(customElement);
            return this;
        }

        public Builder addElements(Collection<CustomElement> customElements) {
            this.elements.addAll(customElements);
            return this;
        }

        public Builder addElements(CustomElement... customElements) {
            for (CustomElement customElement : customElements) {
                this.addElement(customElement);
            }
            return this;
        }

        public CustomForm build() {
            return new CustomForm(this.title, this.icon, this.elements);
        }
    }
}
