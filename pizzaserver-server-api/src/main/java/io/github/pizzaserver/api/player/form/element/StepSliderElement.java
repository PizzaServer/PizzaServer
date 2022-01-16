package io.github.pizzaserver.api.player.form.element;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StepSliderElement extends CustomElement {

    private final List<String> steps;

    @SerializedName("default")
    private final int defaultIndex;


    public StepSliderElement(String text, List<String> steps, int index) {
        super(ElementType.STEP_SLIDER, text);
        this.steps = steps;
        this.defaultIndex = index;
    }

    public StepSliderElement(String text, List<String> steps) {
        this(text, steps, 0);
    }

    public List<String> getSteps() {
        return Collections.unmodifiableList(this.steps);
    }

    public int getDefaultIndex() {
        return this.defaultIndex;
    }


    public static class Builder {

        private String text = "";
        private final List<String> steps = new ArrayList<>();
        private int defaultIndex;


        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder addStep(String step) {
            this.steps.add(step);
            return this;
        }

        public Builder addSteps(List<String> steps) {
            this.steps.addAll(steps);
            return this;
        }

        public Builder addSteps(String... steps) {
            for (String step : steps) {
                this.addStep(step);
            }
            return this;
        }

        public Builder setDefaultIndex(int index) {
            this.defaultIndex = index;
            return this;
        }

        public StepSliderElement build() {
            return new StepSliderElement(this.text, this.steps, this.defaultIndex);
        }
    }
}
