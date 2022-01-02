package io.github.pizzaserver.api.player.form.element;

import com.google.gson.annotations.SerializedName;

public class InputElement extends CustomElement {

    private final String placeholder;

    @SerializedName("default")
    private final String value;


    public InputElement(String elementText) {
        this(elementText, "");
    }

    public InputElement(String elementText, String value) {
        this(elementText, value, "");
    }

    public InputElement(String elementText, String value, String placeholder) {
        super(ElementType.INPUT, elementText);
        this.value = value;
        this.placeholder = placeholder;
    }

    /**
     * Text displayed when no characters are in the input box.
     * @return the text
     */
    public String getPlaceholder() {
        return this.placeholder;
    }

    /**
     * Default text placed in the input box.
     * @return the text
     */
    public String getValue() {
        return this.value;
    }


    public static class Builder {

        private String text = "";
        private String value = "";
        private String placeholder = "";


        /**
         * Set the text displayed above the input element.
         * @param text text
         * @return self for chaining
         */
        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        /**
         * Set the default text placed in.
         * @param value text
         * @return self for chaining
         */
        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        /**
         * Set the placeholder displayed when no text is in the input.
         * @param placeholder text
         * @return self for chaining
         */
        public Builder setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public InputElement build() {
            return new InputElement(this.text, this.value, this.placeholder);
        }

    }

}
