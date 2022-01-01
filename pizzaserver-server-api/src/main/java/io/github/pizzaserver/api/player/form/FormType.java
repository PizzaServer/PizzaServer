package io.github.pizzaserver.api.player.form;

public enum FormType {
    CUSTOM("custom_form"),
    MODAL("modal"),
    SIMPLE("form");


    private final String jsonId;

    FormType(String jsonId) {
        this.jsonId = jsonId;
    }

    public String getJsonId() {
        return this.jsonId;
    }
}
