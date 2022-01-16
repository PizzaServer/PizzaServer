package io.github.pizzaserver.api.player.form.response;

import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.form.Form;

public abstract class FormResponse<T extends Form> {

    protected final T form;
    protected final Player player;
    protected final boolean closed;


    protected FormResponse(T form, Player player, boolean closed) {
        this.form = form;
        this.player = player;
        this.closed = closed;
    }

    public T getForm() {
        return this.form;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean wasClosed() {
        return this.closed;
    }
}
