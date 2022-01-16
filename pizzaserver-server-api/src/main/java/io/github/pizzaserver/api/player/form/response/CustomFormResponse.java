package io.github.pizzaserver.api.player.form.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.form.CustomForm;
import io.github.pizzaserver.api.player.form.element.CustomElement;
import io.github.pizzaserver.api.player.form.element.ElementType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomFormResponse extends FormResponse<CustomForm> {

    private final Map<ElementType, List<Object>> responses = new HashMap<>();

    protected CustomFormResponse(CustomForm form, Player player, boolean closed, JsonArray jsonResponses) {
        super(form, player, closed);

        // Sort responses by type
        for (int i = 0; i < jsonResponses.size(); i++) {
            CustomElement element = form.getElements().get(i);
            if (element.getType() != ElementType.LABEL) {
                JsonPrimitive response = jsonResponses.get(i).getAsJsonPrimitive();
                if (!this.responses.containsKey(element.getType()) && element.getType() != ElementType.LABEL) {
                    this.responses.put(element.getType(), new ArrayList<>());
                }

                switch (element.getType()) {
                    case DROPDOWN:
                    case STEP_SLIDER:
                        this.responses.get(element.getType()).add(response.getAsInt());
                        break;
                    case SLIDER:
                        this.responses.get(element.getType()).add(response.getAsFloat());
                        break;
                    case INPUT:
                        this.responses.get(element.getType()).add(response.getAsString());
                        break;
                    case TOGGLE:
                        this.responses.get(element.getType()).add(response.getAsBoolean());
                        break;
                }
            }
        }
    }

    public int getDropdownResponse(int dropdownIndex) {
        this.checkResponseAvailability(ElementType.DROPDOWN);
        return (int) this.responses.get(ElementType.DROPDOWN).get(dropdownIndex);
    }

    public String getInputResponse(int inputIndex) {
        this.checkResponseAvailability(ElementType.INPUT);
        return (String) this.responses.get(ElementType.INPUT).get(inputIndex);
    }

    public float getSliderResponse(int sliderIndex) {
        this.checkResponseAvailability(ElementType.SLIDER);
        return (float) this.responses.get(ElementType.SLIDER).get(sliderIndex);
    }

    public int getStepSliderResponse(int stepSliderIndex) {
        this.checkResponseAvailability(ElementType.STEP_SLIDER);
        return (int) this.responses.get(ElementType.STEP_SLIDER).get(stepSliderIndex);
    }

    public boolean getToggleResponse(int toggleIndex) {
        this.checkResponseAvailability(ElementType.TOGGLE);
        return (boolean) this.responses.get(ElementType.TOGGLE).get(toggleIndex);
    }

    private void checkResponseAvailability(ElementType type) {
        if (this.wasClosed()) {
            throw new IllegalStateException("This form was closed.");
        }
        if (!this.responses.containsKey(type)) {
            throw new NullPointerException("This form does not have a " + type);
        }
    }


    public static class Builder {

        private CustomForm form;
        private Player player;
        private boolean closed;
        private JsonArray responses;

        public Builder setForm(CustomForm form) {
            this.form = form;
            return this;
        }

        public Builder setPlayer(Player player) {
            this.player = player;
            return this;
        }

        public Builder setClosed(boolean closed) {
            this.closed = closed;
            return this;
        }

        public Builder setResponses(JsonArray responses) {
            this.responses = responses;
            return this;
        }

        public CustomFormResponse build() {
            return new CustomFormResponse(this.form, this.player, this.closed, this.responses);
        }
    }
}
