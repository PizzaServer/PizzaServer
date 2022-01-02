package io.github.pizzaserver.api.player.form.element;

import com.google.gson.annotations.SerializedName;

public class SliderElement extends CustomElement {

    private final int min;
    private final int max;
    private final int step;
    @SerializedName("default")
    private final int value;

    protected SliderElement(String text, int value, int min, int max, int step) {
        super(ElementType.SLIDER, text);
        this.value = value;
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }

    public int getStep() {
        return this.step;
    }

    public int getValue() {
        return this.value;
    }


    public static class Builder {

        private String text = "";
        private int min;
        private int max;
        private int step;
        private int value;

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setMin(int min) {
            this.min = min;
            return this;
        }

        public Builder setMax(int max) {
            this.max = max;
            return this;
        }

        public Builder setStep(int step) {
            this.step = step;
            return this;
        }

        public Builder setValue(int value) {
            this.value = value;
            return this;
        }

        public SliderElement build() {
            return new SliderElement(this.text, this.value, this.min, this.max, this.step);
        }

    }

}
