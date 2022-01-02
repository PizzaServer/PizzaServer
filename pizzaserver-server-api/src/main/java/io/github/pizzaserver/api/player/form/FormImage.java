package io.github.pizzaserver.api.player.form;

import com.google.gson.annotations.SerializedName;

public class FormImage {

    private final Type type;
    private final String data;

    public FormImage(Type type, String value) {
        this.type = type;
        this.data = value;
    }

    public Type getType() {
        return this.type;
    }

    public String getValue() {
        return this.data;
    }


    public enum Type {
        @SerializedName("url")
        URL,

        @SerializedName("path")
        PATH
    }

}
