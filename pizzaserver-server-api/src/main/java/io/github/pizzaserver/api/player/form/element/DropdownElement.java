package io.github.pizzaserver.api.player.form.element;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DropdownElement extends CustomElement {

    private final List<String> options;

    @SerializedName("default")
    private final int defaultOption;

    public DropdownElement(String text, List<String> options, int defaultOption) {
        super(ElementType.DROPDOWN, text);
        this.options = options;
        this.defaultOption = defaultOption;
    }

    public DropdownElement(String text, List<String> options) {
        this(text, options, 0);
    }

    public List<String> getOptions() {
        return Collections.unmodifiableList(this.options);
    }

    public int getDefaultOptionIndex() {
        return this.defaultOption;
    }


    public static class Builder {

        private String text = "";
        private final List<String> options = new ArrayList<>();
        private int defaultOption;


        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder addOption(String option) {
            this.options.add(option);
            return this;
        }

        public Builder addOptions(List<String> options) {
            this.options.addAll(options);
            return this;
        }

        public Builder addOptions(String ...options) {
            for (String option : options) {
                this.addOption(option);
            }
            return this;
        }

        public Builder setDefaultOptionIndex(int index) {
            this.defaultOption = index;
            return this;
        }

        public DropdownElement build() {
            return new DropdownElement(this.text, this.options, this.defaultOption);
        }

    }


}
