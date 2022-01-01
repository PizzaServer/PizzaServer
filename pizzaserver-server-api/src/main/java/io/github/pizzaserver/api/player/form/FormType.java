package io.github.pizzaserver.api.player.form;

public enum FormType {
    CUSTOM("custom"),
    MODAL("modal"),
    SIMPLE("simple");


    private final String jsonId;

    FormType(String jsonId) {
        this.jsonId = jsonId;
    }

    public String getJsonId() {
        return this.jsonId;
    }
}
