package io.github.pizzaserver.api.player.form;

public class ModalForm extends Form {

    private final String content;

    // For serialization purposes, these are named button1 and button2 as Minecraft sends them over the network as such
    private final String button1;
    private final String button2;

    protected ModalForm(String title, String content, String trueButton, String falseButton) {
        super(FormType.MODAL, title);
        this.content = content;
        this.button1 = trueButton;
        this.button2 = falseButton;
    }

    public String getContent() {
        return this.content;
    }

    public String getTrueButton() {
        return this.button1;
    }

    public String getFalseButton() {
        return this.button2;
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
            return new ModalForm(this.title, this.content, this.trueButton, this.falseButton);
        }

    }

}
