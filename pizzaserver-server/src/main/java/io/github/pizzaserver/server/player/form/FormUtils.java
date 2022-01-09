package io.github.pizzaserver.server.player.form;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.form.CustomForm;
import io.github.pizzaserver.api.player.form.Form;
import io.github.pizzaserver.api.player.form.ModalForm;
import io.github.pizzaserver.api.player.form.SimpleForm;
import io.github.pizzaserver.api.player.form.element.*;
import io.github.pizzaserver.api.player.form.response.CustomFormResponse;
import io.github.pizzaserver.api.player.form.response.FormResponse;
import io.github.pizzaserver.api.player.form.response.ModalFormResponse;
import io.github.pizzaserver.api.player.form.response.SimpleFormResponse;
import io.github.pizzaserver.server.ImplServer;

public class FormUtils {

    protected static final Gson GSON = new Gson();


    private FormUtils() {}

    public static String toJSON(Form form) {
        return GSON.toJson(form);
    }

    public static FormResponse<? extends Form> toResponse(Form form, Player player, String data) {
        boolean closed = data.equals("null");
        switch (form.getType()) {
            case MODAL:
                return new ModalFormResponse.Builder()
                        .setForm((ModalForm) form)
                        .setPlayer(player)
                        .setResult(data.equals("true"))
                        .setClosed(closed)
                        .build();
            case SIMPLE:
                int simpleResponse = 0;
                try {
                    simpleResponse = Integer.parseInt(data);
                } catch (NumberFormatException exception) {
                    ImplServer.getInstance().getLogger().debug("Invalid integer provided in simple form response from client.");
                    closed = true;
                }

                if (simpleResponse < 0 || simpleResponse >= ((SimpleForm) form).getButtons().size()) {
                    ImplServer.getInstance().getLogger().debug("Out of bounds integer provided in simple form response from client.");
                    closed = true;
                }

                return new SimpleFormResponse.Builder()
                        .setForm((SimpleForm) form)
                        .setPlayer(player)
                        .setClosed(closed)
                        .setResponse(simpleResponse)
                        .build();
            case CUSTOM:
                JsonArray responses = closed ? new JsonArray() : GSON.fromJson(data, JsonArray.class);

                // Check if invalid response amount was returned.
                if (responses.size() != ((CustomForm) form).getElements().size()) {
                    ImplServer.getInstance().getLogger().debug("Invalid amount of form response elements returned by client.");
                    closed = true;
                    responses = new JsonArray();
                }

                // Check if responses are invalid.
                for (int i = 0; i < responses.size(); i++) {
                    CustomElement customElement = ((CustomForm) form).getElements().get(i);
                    if (isInvalidElementResponse(responses.get(i), customElement)) {
                        ImplServer.getInstance().getLogger().debug("Invalid response for custom form element returned by client.");
                        closed = true;
                        responses = new JsonArray();
                        break;
                    }
                }

                return new CustomFormResponse.Builder()
                        .setForm((CustomForm) form)
                        .setPlayer(player)
                        .setClosed(closed)
                        .setResponses(responses)
                        .build();
        }

        throw new AssertionError("Unknown form response type");
    }

    private static boolean isInvalidElementResponse(JsonElement response, CustomElement element) {
        if (element.getType() == ElementType.LABEL) {
            return false;
        }

        if (!response.isJsonPrimitive()) {
            return true;
        }
        JsonPrimitive primitive = response.getAsJsonPrimitive();

        switch (element.getType()) {
            case DROPDOWN:
                if (!primitive.isNumber()) {
                    return true;
                }
                int dropdownIndex = primitive.getAsInt();
                return dropdownIndex < 0 || dropdownIndex >= ((DropdownElement) element).getOptions().size();
            case INPUT:
                return !primitive.isString();
            case SLIDER:
                if (!primitive.isNumber()) {
                    return true;
                }
                int sliderValue = primitive.getAsInt();
                return sliderValue < ((SliderElement) element).getMin() || sliderValue > ((SliderElement) element).getMax();
            case STEP_SLIDER:
                if (!primitive.isNumber()) {
                    return true;
                }
                int stepSliderValue = primitive.getAsInt();
                return stepSliderValue < 0 || stepSliderValue >= ((StepSliderElement) element).getSteps().size();
            case TOGGLE:
                return !primitive.isBoolean();
            default:
                return true;
        }
    }

}
