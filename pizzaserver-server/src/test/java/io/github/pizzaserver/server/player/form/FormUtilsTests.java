package io.github.pizzaserver.server.player.form;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.form.CustomForm;
import io.github.pizzaserver.api.player.form.FormImage;
import io.github.pizzaserver.api.player.form.ModalForm;
import io.github.pizzaserver.api.player.form.SimpleForm;
import io.github.pizzaserver.api.player.form.element.*;
import io.github.pizzaserver.api.player.form.response.CustomFormResponse;
import io.github.pizzaserver.api.player.form.response.ModalFormResponse;
import io.github.pizzaserver.api.player.form.response.SimpleFormResponse;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FormUtilsTests {

    @Test
    public void shouldSerializeModalTypeCorrectly() throws IOException {
        ModalForm modalForm = new ModalForm.Builder()
                .setTitle("Title")
                .setContent("Content")
                .setTrueButton("True Button")
                .setFalseButton("False Button")
                .build();

        JsonObject correctJSON = FormUtils.GSON.fromJson(IOUtils.toString(FormUtilsTests.class.getResourceAsStream("/form/modal.json"), StandardCharsets.UTF_8), JsonObject.class);
        JsonObject outputJSON = FormUtils.GSON.fromJson(FormUtils.toJSON(modalForm), JsonObject.class);
        assertEquals(correctJSON, outputJSON, "Modal form JSON does not match.");
    }

    @Test
    public void shouldSerializeCustomTypeCorrectly() throws IOException {
        CustomForm form = new CustomForm.Builder()
                .setTitle("Title")
                .setIcon(new FormImage(FormImage.Type.PATH, "path to image"))
                .addElement(new DropdownElement.Builder()
                        .setText("Dropdown")
                        .setDefaultOptionIndex(1)
                        .addOptions("A", "B", "C")
                        .build())
                .addElement(new InputElement("Input", "value", "placeholder"))
                .addElement(new LabelElement("Label"))
                .addElement(new SliderElement.Builder()
                        .setText("Slider")
                        .setMin(0)
                        .setMax(10)
                        .setStep(1)
                        .setValue(5)
                        .build())
                .addElement(new StepSliderElement.Builder()
                        .setText("StepSlider")
                        .addSteps("A", "B", "C")
                        .setDefaultIndex(2)
                        .build())
                .addElement(new ToggleElement("Toggle", true))
                .build();

        JsonObject correctJSON = FormUtils.GSON.fromJson(IOUtils.toString(FormUtilsTests.class.getResourceAsStream("/form/custom.json"), StandardCharsets.UTF_8), JsonObject.class);
        JsonObject outputJSON = FormUtils.GSON.fromJson(FormUtils.toJSON(form), JsonObject.class);
        assertEquals(correctJSON, outputJSON, "Custom form JSON does not match.");
    }

    @Test
    public void shouldSerializeSimpleTypeCorrectly() throws IOException {
        SimpleForm form = new SimpleForm.Builder()
                .setTitle("Title")
                .setContent("Content")
                .addButton(new ButtonElement("Button One"))
                .addButton(new ButtonElement("Button Two", new FormImage(FormImage.Type.URL, "url to image")))
                .addButton(new ButtonElement("Button Three", new FormImage(FormImage.Type.PATH, "path to image")))
                .build();

        JsonObject correctJSON = FormUtils.GSON.fromJson(IOUtils.toString(FormUtilsTests.class.getResourceAsStream("/form/simple.json"), StandardCharsets.UTF_8), JsonObject.class);
        JsonObject outputJSON = FormUtils.GSON.fromJson(FormUtils.toJSON(form), JsonObject.class);
        assertEquals(correctJSON, outputJSON, "Simple form JSON does not match.");
    }

    @Test
    public void shouldDeserializeModalTypeResponseCorrectly() throws IOException {
        String response = IOUtils.toString(FormUtilsTests.class.getResourceAsStream("/form/response/modal.txt"), StandardCharsets.UTF_8);

        ModalFormResponse formResponse = (ModalFormResponse) FormUtils.toResponse(new ModalForm.Builder()
                .build(), mock(Player.class), response);
        assertTrue(formResponse.getResult());
    }

    @Test
    public void shouldDeserializeCustomTypeResponseCorrectly() throws IOException {
        String response = IOUtils.toString(FormUtilsTests.class.getResourceAsStream("/form/response/custom.json"), StandardCharsets.UTF_8);

        CustomForm form = new CustomForm.Builder()
                .setTitle("Title")
                .setIcon(new FormImage(FormImage.Type.PATH, "path to image"))
                .addElement(new ToggleElement("Toggle", true))
                .addElement(new DropdownElement.Builder()
                        .setText("Dropdown")
                        .setDefaultOptionIndex(1)
                        .addOptions("A", "B", "C")
                        .build())
                .addElement(new InputElement("Input", "value", "placeholder"))
                .addElement(new LabelElement("Label"))
                .addElement(new SliderElement.Builder()
                        .setText("Slider")
                        .setMin(0)
                        .setMax(10)
                        .setStep(1)
                        .setValue(5)
                        .build())
                .addElement(new StepSliderElement.Builder()
                        .setText("StepSlider")
                        .addSteps("A", "B", "C")
                        .setDefaultIndex(2)
                        .build())
                .addElement(new ToggleElement("Toggle", false))
                .build();

        CustomFormResponse formResponse = (CustomFormResponse) FormUtils.toResponse(form, mock(Player.class), response);

        assertEquals(0, formResponse.getDropdownResponse(0), "Dropdown input failed to be parsed.");
        assertEquals("input", formResponse.getInputResponse(0), "Text input failed to be parsed.");
        assertEquals(7, formResponse.getSliderResponse(0), "Slider response failed to be parsed.");
        assertEquals(2, formResponse.getStepSliderResponse(0), "Step slider failed to be parsed.");
        assertTrue(formResponse.getToggleResponse(0), "Toggle response failed to be parsed.");
        assertFalse(formResponse.getToggleResponse(1), "Second toggle response failed to be parsed.");
    }

    @Test
    public void shouldDeserializeSimpleTypeResponseCorrectly() throws IOException {
        String response = IOUtils.toString(FormUtilsTests.class.getResourceAsStream("/form/response/simple.txt"), StandardCharsets.UTF_8);

        SimpleForm form = new SimpleForm.Builder()
                .setTitle("Title")
                .setContent("Content")
                .addButton(new ButtonElement("Button One"))
                .addButton(new ButtonElement("Button Two", new FormImage(FormImage.Type.URL, "url to image")))
                .addButton(new ButtonElement("Button Three", new FormImage(FormImage.Type.PATH, "path to image")))
                .build();

        SimpleFormResponse formResponse = (SimpleFormResponse) FormUtils.toResponse(form, mock(Player.class), response);
        assertEquals(1, formResponse.getResponse());
    }

}
