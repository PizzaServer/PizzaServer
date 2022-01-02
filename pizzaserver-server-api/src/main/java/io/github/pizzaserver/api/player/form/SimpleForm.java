package io.github.pizzaserver.api.player.form;

import io.github.pizzaserver.api.player.form.element.ButtonElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple forms only consist of text and buttons.
 */
public class SimpleForm extends Form {

    protected final String content;
    protected final List<ButtonElement> buttons;

    protected SimpleForm(String title, String content, List<ButtonElement> buttons) {
        super(FormType.SIMPLE, title);
        this.content = content;
        this.buttons = buttons;
    }

    public String getContent() {
        return this.content;
    }

    public List<ButtonElement> getButtons() {
        return this.buttons;
    }

    public ButtonElement getButton(int buttonIndex) {
        return this.buttons.get(buttonIndex);
    }


    public static class Builder {

        private String title;
        private String content;
        private final List<ButtonElement> buttons = new ArrayList<>();


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder addButton(ButtonElement button) {
            this.buttons.add(button);
            return this;
        }

        public Builder addButtons(List<ButtonElement> buttons) {
            this.buttons.addAll(buttons);
            return this;
        }

        public Builder addButtons(ButtonElement ...buttons) {
            for (ButtonElement button : buttons) {
                this.addButton(button);
            }
            return this;
        }

        public SimpleForm build() {
            return new SimpleForm(this.title, this.content, this.buttons);
        }

    }

}
