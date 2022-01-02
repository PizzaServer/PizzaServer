package io.github.pizzaserver.api.player.form.response;

import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.form.SimpleForm;

public class SimpleFormResponse extends FormResponse<SimpleForm> {

    private final int response;

    protected SimpleFormResponse(SimpleForm form, Player player, boolean closed, int response) {
        super(form, player, closed);
        this.response = response;
    }

    public int getResponse() {
        return this.response;
    }


    public static class Builder {

        private SimpleForm form;
        private Player player;
        private boolean closed;
        private int response;


        public Builder setForm(SimpleForm form) {
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

        public Builder setResponse(int response) {
            this.response = response;
            return this;
        }

        public SimpleFormResponse build() {
            return new SimpleFormResponse(this.form, this.player, this.closed, this.response);
        }

    }

}
