package io.github.pizzaserver.api.player.form.element;

import com.google.gson.annotations.SerializedName;

public class SliderElement extends CustomElement {

    private final float min;
    private final float max;
    private final float step;
    @SerializedName("default")
    private final float value;

    protected SliderElement(String text, float value, float min, float max, float step) {
        super(ElementType.SLIDER, text);
        this.value = value;
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }

    public float getStep() {
        return this.step;
    }

    public float getValue() {
        return this.value;
    }


    public static class Builder {

        private String text = "";
        private float min;
        private float max;
        private float step = 1;
        private float value;

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setMin(float min) {
            this.min = min;
            return this;
        }

        public Builder setMax(float max) {
            this.max = max;
            return this;
        }

        /**
         * Sets the slider's step from one value to the next.
         * If the step is not an integral value (multiple of 1), values will be displayed with decimals.
         * @param step Step value. Must not be zero.
         */
        public Builder setStep(float step) {
            this.step = step;
            return this;
        }

        public Builder setValue(float value) {
            this.value = value;
            return this;
        }

        public SliderElement build() {
            return new SliderElement(this.text, this.value, this.min, this.max, this.step);
        }

    }

}
