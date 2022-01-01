package io.github.pizzaserver.server.player.form;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.form.FormType;
import io.github.pizzaserver.api.player.form.ModalForm;
import io.github.pizzaserver.api.player.form.response.ModalFormResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class FormUtilsTests {

    private final static Gson GSON = new Gson();

    @Test
    public void shouldSerializeModalTypeCorrectly() {
        String title = "Title";
        String content = "Content";
        String trueButton = "True Button";
        String falseButton = "False Button";

        ModalForm modalForm = new ModalForm.Builder()
                .setTitle(title)
                .setContent(content)
                .setTrueButton(trueButton)
                .setFalseButton(falseButton)
                .build();

        JsonObject correctJSON = new JsonObject();
        correctJSON.addProperty("type", FormType.MODAL.getJsonId());
        correctJSON.addProperty("title", title);
        correctJSON.addProperty("content", content);
        correctJSON.addProperty("button1", trueButton);
        correctJSON.addProperty("button2", falseButton);

        JsonObject outputJSON = GSON.fromJson(FormUtils.toJSON(modalForm), JsonObject.class);

        assertEquals(correctJSON, outputJSON, "Modal form JSON does not match.");
    }

    @Test
    public void shouldSerializeCustomTypeCorrectly() {

    }

    @Test
    public void shouldSerializeSimpleTypeCorrectly() {

    }

    @Test
    public void shouldDeserializeModalTypeResponseCorrectly() {
        String response = "true\n";

        ModalFormResponse formResponse = (ModalFormResponse) FormUtils.toResponse(new ModalForm.Builder()
                .build(), mock(Player.class), response);
        assertTrue(formResponse.getResult());
    }

    @Test
    public void shouldDeserializeCustomTypeResponseCorrectly() {

    }

    @Test
    public void shouldDeserializeSimpleTypeResponseCorrectly() {

    }

}
