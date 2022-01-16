package io.github.pizzaserver.api.player.form;

import com.google.gson.annotations.SerializedName;

public enum FormType {
    @SerializedName("custom_form") CUSTOM,

    @SerializedName("modal") MODAL,

    @SerializedName("form") SIMPLE;
}
