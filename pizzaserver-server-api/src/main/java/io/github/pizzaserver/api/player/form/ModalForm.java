package io.github.pizzaserver.api.player.form;

import com.google.gson.annotations.SerializedName;
import io.github.pizzaserver.commons.utils.Check;

/**
 * Modal forms are forms with only a true and false option that players can select.
 */
public class ModalForm extends Form {

    private final String content;

    @SerializedName("button1")
    private final String trueButton;

    @SerializedName("button2")
    private final String falseButton;

    protected ModalForm(String title, String content, String trueButton, String falseButton) {
        super(FormType.MODAL, title);
        this.content = content;
        this.trueButton = trueButton;
        this.falseButton = falseButton;
    }

    public String getContent() {
        return this.content;
    }

    public String getTrueButton() {
        return this.trueButton;
    }

    public String getFalseButton() {
        return this.falseButton;
    }


    public static class Builder {

        private String title = "";
        private String content = "";
        private String trueButton = "gui.yes";
        private String falseButton = "gui.no";


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setTrueButton(String trueButton) {
            this.trueButton = trueButton;
            return this;
        }

        public Builder setFalseButton(String falseButton) {
            this.falseButton = falseButton;
            return this;
        }


        public ModalForm build() {
            return new ModalForm(Check.notNull(this.title, "title"),
                    Check.notNull(this.content, "content"),
                    Check.notNull(this.trueButton, "true button text"),
                    Check.notNull(this.falseButton, "false button text"));
        }

    }

}
