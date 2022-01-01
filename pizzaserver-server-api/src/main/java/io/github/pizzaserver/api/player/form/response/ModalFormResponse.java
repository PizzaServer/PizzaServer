package io.github.pizzaserver.api.player.form.response;

import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.form.ModalForm;


public class ModalFormResponse extends FormResponse<ModalForm> {

    private final boolean result;


    protected ModalFormResponse(ModalForm form, Player player, boolean closed, boolean result) {
        super(form, player, closed);
        this.result = result;
    }

    public boolean getResult() {
        return this.result;
    }


    public static class Builder {

        private ModalForm form;
        private Player player;
        private boolean closed;
        private boolean result;

        public Builder setForm(ModalForm form) {
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

        public Builder setResult(boolean result) {
            this.result = result;
            return this;
        }

        public ModalFormResponse build() {
            return new ModalFormResponse(this.form, this.player, this.closed, this.result);
        }

    }

}
