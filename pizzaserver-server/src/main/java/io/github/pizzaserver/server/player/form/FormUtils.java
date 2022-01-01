package io.github.pizzaserver.server.player.form;

import com.google.gson.*;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.form.Form;
import io.github.pizzaserver.api.player.form.FormType;
import io.github.pizzaserver.api.player.form.ModalForm;
import io.github.pizzaserver.api.player.form.response.FormResponse;
import io.github.pizzaserver.api.player.form.response.ModalFormResponse;

import java.lang.reflect.Type;

public class FormUtils {

    private static final Gson GSON = new Gson().newBuilder()
            .registerTypeAdapter(FormType.class, new JsonSerializer<FormType>() {
                @Override
                public JsonElement serialize(FormType formType, Type type, JsonSerializationContext jsonSerializationContext) {
                    return new JsonPrimitive(formType.getJsonId());
                }
            })
            .create();


    private FormUtils() {}

    public static String toJSON(Form form) {
        return GSON.toJson(form);
    }

    public static FormResponse<? extends Form> toResponse(Form form, Player player, String data) {
        switch (form.getType()) {
            case MODAL:
                return new ModalFormResponse.Builder()
                        .setForm((ModalForm) form)
                        .setPlayer(player)
                        .setResult(data.contains("true"))
                        .build();
            case SIMPLE:
                break;
            case CUSTOM:
                break;
        }

        throw new AssertionError("Unknown form response type");
    }

}
